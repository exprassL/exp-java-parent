package com.exp.web.aop.annotation;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.aop.aspect.String2HexAspect;

import java.lang.annotation.*;

/**
 * 注解在方法上，表示其返回值中，由{@link FieldString2Hex}注解的字段将被执行{@link StringUtils#str2hex(String)}转换
 *
 * @see String2HexAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface String2HexPointCut {
    
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FieldString2Hex {}
}
