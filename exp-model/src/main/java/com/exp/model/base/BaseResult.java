package com.exp.model.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口响应基类
 */
@Getter
@Setter
public class BaseResult {
    
    /**
     * 数据获取成功与否
     */
    private Boolean success = Boolean.TRUE;
    
    /**
     * 消息
     */
    private String message;
    
}