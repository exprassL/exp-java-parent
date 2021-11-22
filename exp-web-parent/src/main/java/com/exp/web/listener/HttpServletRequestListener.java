package com.exp.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * Request事件侦听
 */
@Slf4j
@Component
public class HttpServletRequestListener implements ServletRequestListener {

    /**
     * Request初始化
     *
     * @param servletRequestEvent
     */
    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest)servletRequestEvent.getServletRequest();
        log.info("Request Begin < < < < < < < < < < < <");
        log.info("URI: {}", request.getRequestURI());
        log.info("Method: {}", request.getMethod());
        log.info("Referer: {}", request.getHeader("referer"));
    }

    /**
     * Request执行完成销毁时
     * @param servletRequestEvent
     */
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest request = (HttpServletRequest)servletRequestEvent.getServletRequest();
        log.info("URI: {}", request.getRequestURI());
        log.info("> > > > > > > > > > > > Request End");
    }
}