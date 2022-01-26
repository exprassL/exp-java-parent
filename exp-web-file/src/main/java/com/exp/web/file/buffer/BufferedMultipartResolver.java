package com.exp.web.file.buffer;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.file.pojo.FileUploadProgress;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <ol>
 * <li>
 * 重写{@link CommonsMultipartResolver#parseRequest(HttpServletRequest)}，
 * 增加上传进度监听；
 * </li>
 * <li>
 * 重写{@link CommonsMultipartResolver#cleanupFileItems(MultiValueMap)}，
 * TODO what??
 * </li>
 * </ol>
 */
@Slf4j
@Component
public class BufferedMultipartResolver extends CommonsMultipartResolver {
    
    private static final ThreadLocal<String> PROGRESS_ID_HOLDER = new ThreadLocal<>();
    
    /**
     * 返回当前上传进度id
     *
     * @return
     */
    public static String getSegmentId() {
        return PROGRESS_ID_HOLDER.get();
    }
    
    /**
     * 重写以支持上传进度。
     * 上传进度指一次上传整体的进度，而不是单个文件的进度；若要对多个文件进行进度监听，请一次上传一个文件。
     *
     * @param request
     * @return
     * @throws MultipartException
     */
    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        final String uuid = StringUtils.uuid();
        PROGRESS_ID_HOLDER.set(uuid);
    
        fileUpload.setProgressListener(new ProgressListener() {
            
            private HttpSession session;
            
            private ProgressListener setSession(HttpSession session) {
                this.session = session;
                return this;
            }
    
            /**
             * 这里的bytes实际是整个上传请求的payload大小，不单指文件，还包括其他参数
             */
            @Override
            public void update(long pBytesRead, long pContentLength, int pItems) {
                String segmentId = "segment-" + uuid;
                log.info("文件片段(: {})上传进度：{} / {}", segmentId, pBytesRead, pContentLength);
                session.setAttribute(segmentId, new FileUploadProgress()
                        .setItems(pItems).setTotal(pContentLength).setRead(pBytesRead));
                // 如果不放到session，也可以放到一个公共Map中
            }
            
        }.setSession(request.getSession()));
        
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadBase.FileSizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getFileSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Failed to parse multipart servlet request", ex);
        }
    }
    
    @Override
    protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles) {
        for (List<MultipartFile> files : multipartFiles.values()) {
            for (MultipartFile file : files) {
                if (file instanceof CommonsMultipartFile) {
                    // TODO FIXME 在此处调用缓存方法？
                    log.info("请求结束，缓存文件待用: {}", file.getName());
                }
            }
        }
    }

}
