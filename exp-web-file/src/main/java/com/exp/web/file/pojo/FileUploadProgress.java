package com.exp.web.file.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文件上传进度
 */
@Data
@Accessors(chain = true)
public class FileUploadProgress implements Serializable {
    
    private static final long serialVersionUID = -6679427758582815561L;
    /**
     * 已经读取的bytes
     */
    private long read;
    
    /**
     * 总的bytes
     */
    private long total;
    
    /**
     * 当前已经读取的文件数
     */
    private int items;
}
