package com.exp.web.file.pojo;

import com.exp.web.aop.annotation.String2HexPointCut;
import lombok.Data;

import java.util.Date;

/**
 * 文件基本信息
 */
@Data
public class FileInfo {
    
    /**
     * 文件名
     */
    private String name;
    
    /**
     * 相对于存储根目录的文件路径
     */
    @String2HexPointCut.FieldString2Hex
    private String path;
    
    /**
     * 当前路径的父路径
     */
    @String2HexPointCut.FieldString2Hex
    private String parentPath;
    
    /**
     * 是否是文件
     */
    private boolean isFile;
    
    /**
     * 文件类型或后缀
     */
    private String ext;
    
    /**
     * 最后修改日期
     */
    private Date lastModifiedDate;
    
}
