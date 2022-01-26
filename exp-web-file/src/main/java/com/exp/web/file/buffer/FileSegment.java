package com.exp.web.file.buffer;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileSegment {
    
    /**
     * 文件上传临时唯一标识
     */
    private String uuid;
    
    /**
     * 文件（不是文件片段）临时唯一标识
     */
    private String id;
    
    /**
     * 文件或文件片段（可能需要切片上传），当作一个文件处理
     * 从中可获取文件原始名称
     */
    private MultipartFile segment;
    
    /**
     * 切片总数
     */
    private Integer round;
    
    /**
     * 当前第几个切片
     */
    private Integer segNo;
}
