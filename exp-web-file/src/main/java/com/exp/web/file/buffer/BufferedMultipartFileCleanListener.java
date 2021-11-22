package com.exp.web.file.buffer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监听Session事件
 */
@Slf4j
@Component
public class BufferedMultipartFileCleanListener implements HttpSessionListener {

    /**
     * Session创建时
     * @param httpSessionEvent
     */
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        String sessionId = httpSessionEvent.getSession().getId();
        log.info("Session Created, Session Id is {}", sessionId);
    }

    /**
     * Session销毁（用户注销、Session超时）时，清空该Session下的缓存文件
     * @param httpSessionEvent
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        String sessionId = httpSessionEvent.getSession().getId();
        log.info("Session Destroyed, Session Id is {}", sessionId);
        BufferPool.clean(sessionId);
    }

}
