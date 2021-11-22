package com.exp.web.exception;


import com.exp.model.base.BaseResult;
import com.exp.model.response.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 所有其他异常处理器未能匹配处理时，在此处理
     *
     * @param e 全局捕获异常
     * @return 异常处理信息
     */
    @ExceptionHandler({Throwable.class})
    private BaseResult handle(Exception e) {
        log.error("捕获全局异常：", e);
        return new ErrorResult().fail(e.getMessage());
    }
}
