package com.exp.web.file.enums;

/**
 * 文件上传业务类型
 */
public enum UploadType {

    ALBUM("相册"),
    
    PORTRAIT("头像"), // 头像，肖像
    
    OTHER("其他"),
    ;

    private String name;

    UploadType(String name) {
        this.name = name;
    }

}
