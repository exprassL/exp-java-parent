package com.exp.web.file.ctrl;

import com.exp.web.file.buffer.BufferPool;
import com.exp.web.file.buffer.BufferedMultipartFile;
import com.exp.web.file.pojo.FileUploadProgress;
import com.exp.model.response.ListResult;
import com.exp.model.response.MapResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文件上传
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class FileUploadCtrl {
    
    /**
     * 文件上传缓存；上传后的文件保存在缓存中，直到用户确认保存到指定位置，或超时清除；
     *
     * @param files
     * @param request
     * @return
     */
    @PostMapping("/buffer")
    public ListResult<BufferedMultipartFile> buffer(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
        ListResult<BufferedMultipartFile> result = new ListResult<>();
        List<BufferedMultipartFile> fileList = result.getDataList();
        
        // 多文件上传
        for (MultipartFile file : files){
            BufferedMultipartFile addedFile = BufferPool.add((CommonsMultipartFile) file, request.getSession().getId());
            if (addedFile == null) {
                continue;
            }
            fileList.add(addedFile);
        }
        return result;
    }
    
    /**
     * 上传进度，指一次上传的整体进度，而不是单个文件的进度。
     * 多文件上传，每个文件都需要监控上传进度时，可以多次上传，每次上传一个文件。
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/progress")
    public MapResult progress(HttpServletRequest request) {
        FileUploadProgress progress = (FileUploadProgress) request.getSession().getAttribute("progress");
        if (progress == null) {
            progress = new FileUploadProgress().setRead(0L).setTotal(1L);
        }
        MapResult result = new MapResult();
        return result.put("progress", progress);
    }
}
