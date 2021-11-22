package com.exp.web.file.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件上传进度
 */
@Data
@Accessors(chain = true)
public class FileUploadProgress {
    
    /**
     * 以读取的bytes
     */
    private long read;
    
    /**
     * 总的bytes
     */
    private long total;
    
    /**
     * 当前以读取的文件数
     */
    private int items;
}
