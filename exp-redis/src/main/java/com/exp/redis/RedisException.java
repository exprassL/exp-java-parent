package com.exp.redis;

import com.exp.toolkit.basic.StringUtils;

/**
 * Redis操作异常
 */
public class RedisException extends Exception {
    
    private RedisException(String message, Throwable cause) {
        super(message, cause);
    }
    
    private RedisException(String message) {
        super(message);
    }
    
    /**
     * 将捕获的其他异常封装成自定义异常，并将消息参数按照模板封装到此自定义异常
     *
     * @param cause      捕获的其他异常
     * @param template   消息模板
     * @param parameters 消息参数
     * @return 封装的异常
     */
    public static Exception produce(Throwable cause, String template, Object... parameters) {
        String message = StringUtils.format(template, parameters);
        return new RedisException(message, cause);
    }
    
    /**
     * 将消息参数按照模板封装到自定义异常
     *
     * @param template   消息模板
     * @param parameters 消息参数
     * @return 封装的异常
     */
    public static Exception produce(String template, Object... parameters) {
        String message = StringUtils.format(template, parameters);
        return new RedisException(message);
    }
    
}
