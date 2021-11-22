package com.exp.web.configer;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.context.AppContextUtil;
import com.exp.web.exception.Asserts;
import org.springframework.core.env.Environment;

/**
 * 外部配置
 */
public final class WebAppEnvironment {
    
    private static final Environment ENV = AppContextUtil.getBean(Environment.class);
    
    /**
     * 返回当前环境关键字
     *
     * @return
     */
    public static String currentEnv() {
        String env = ENV.getProperty("spring.profiles.active");
        Asserts.isTrue(StringUtils.isNotBlank(env), "未指定环境关键字");
        return env;
    }
    
    /**
     * 是否启动debug，仅local和dev环境启用
     *
     * @return
     */
    public static boolean isDebugEnabled() {
        String active = ENV.getProperty("spring.profiles.active"); // local dev test production
        return active == null || active.equals("local") || active.equals("dev");
    }
    
    /**
     * 将配置中定义的属性name的值转为整数返回，若无此属性，或属性值为空、空白或不能转为整数，则返回默认值
     *
     * @param name         属性名称
     * @param defaultValue 默认值
     * @return  返回整数的属性值；若无此属性，或属性值为空、空白或不能转为整数，则返回默认值
     */
    public static int getIntProperty(String name, int defaultValue) {
        String value = ENV.getProperty(name);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
