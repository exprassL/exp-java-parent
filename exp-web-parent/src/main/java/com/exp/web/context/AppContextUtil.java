package com.exp.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文工具，可获取上下文中的Bean
 */
@Component
public class AppContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContextUtil.applicationContext = applicationContext;
    }
    
    public static String getContextPath() {
        return AppContextUtil.applicationContext.getApplicationName();
    }

    public static Object getBean(String name) {
        return AppContextUtil.applicationContext.getBean(name);
    }

    public static  <B> B getBean(Class<B> clazz, String name) {
        return AppContextUtil.applicationContext.getBean(clazz, name);
    }

    public static <B> B getBean(Class<B> clazz) {
        return AppContextUtil.applicationContext.getBean(clazz);
    }

}