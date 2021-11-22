package com.exp.web.file.service;


import com.exp.toolkit.basic.DateTimeUtils;
import com.exp.web.file.buffer.BufferPool;
import com.exp.web.file.buffer.BufferedMultipartFile;
import com.exp.web.file.enums.UploadType;
import com.exp.model.response.ListResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * 文件上传接口，提供一组默认方法，处理文件保存及之后的业务操作；
 * 引用方需实现该接口，并提供一个<code> Service Bean </code>到<code> Spring </code>容器。
 */
public interface IFileUploadService {
    
    Logger log = LoggerFactory.getLogger(IFileUploadService.class);
    
    /**
     * 文件保存
     *
     * @param type      文件上传业务类型
     * @param root      文件存储根路径
     * @param sessionId 登录session id
     * @param ids       目标缓存文件id
     * @return 缓存的文件
     */
    default ListResult<BufferedMultipartFile> store(UploadType type, String root, String sessionId, String... ids) {
        ListResult<BufferedMultipartFile> result = new ListResult<>();
        List<BufferedMultipartFile> list = result.getDataList();
        String sep = File.separator;
        StringBuilder fileDir = new StringBuilder(root).append(type.name()).append(sep)
                .append(DateTimeUtils.today()).append(sep).append(sessionId);
        BufferPool.getSessionFiles(sessionId, ids).forEach(bufferedFile -> {
            String id = bufferedFile.getId();
            String originalName = bufferedFile.getOriginalName();
            String ext = "";
            if (originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String filePath = fileDir + sep + id + ext;
            try {
                // 文件落盘
                bufferedFile.setStorage(filePath).transfer();
                list.add(bufferedFile);
            } catch (IOException e) {
                log.error("文件[{}]保存失败", originalName, e);
            } finally {
                bufferedFile.getFile().getFileItem().delete();
            }
        });
        return result;
    }
}