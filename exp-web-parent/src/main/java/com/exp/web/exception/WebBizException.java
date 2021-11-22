package com.exp.web.exception;


import com.exp.toolkit.basic.StringUtils;

/**
 * 自定义业务异常，非检查异常
 */
public class WebBizException extends RuntimeException {
    
    private static final long serialVersionUID = 7344157441755003705L;
    
    private WebBizException(String message, Throwable cause) {
        super(message, cause);
    }
    
    private WebBizException(String message) {
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
    public static RuntimeException produce(Throwable cause, String template, Object... parameters) {
        String message = StringUtils.format(template, parameters);
        return new WebBizException(message, cause);
    }
    
    /**
     * 将消息参数按照模板封装到自定义异常
     *
     * @param template   消息模板
     * @param parameters 消息参数
     * @return 封装的异常
     */
    public static RuntimeException produce(String template, Object... parameters) {
        String message = StringUtils.format(template, parameters);
        return new WebBizException(message);
    }
}
