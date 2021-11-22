package com.exp.web.exception;

import static com.exp.web.exception.WebBizException.*;

public final class Asserts {
    
    public static void isNull(Object object) {
        if (object != null) {
            throw produce("参数object应该为空");
        }
    }
    
    public static void isNull(Object object, String template, Object... parameters) {
        if (object != null) {
            throw produce(template, parameters);
        }
    }

    public static void notNull(Object object) {
        if (object != null) {
            throw produce("参数object不能为空");
        }
    }
    
    public static void notNull(Object object, String template, Object... parameters) {
        if (object == null) {
            throw produce(template, parameters);
        }
    }
    
    /**
     * @param expression boolean表达式
     */
    public static void isTrue(boolean expression) {
        if (!expression) {
            throw produce("表达式的计算结果应该是true");
        }
    }
    
    /**
     * @param expression boolean表达式
     */
    public static void isTrue(boolean expression, String template, Object... parameters) {
        if (!expression) {
            throw produce(template, parameters);
        }
    }
    
    /**
     * @param expression boolean表达式
     */
    public static void notTrue(boolean expression) {
        if (expression) {
            throw produce("表达式的计算结果应该是false");
        }
    }
    
    /**
     * @param expression boolean表达式
     */
    public static void notTrue(boolean expression, String template, Object... parameters) {
        if (expression) {
            throw produce(template, parameters);
        }
    }
}