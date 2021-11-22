package com.exp.web.aop.annotation;

import com.exp.web.aop.aspect.Hex2StringAspect;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 注解在方法上，表示其指定参数将被{@link Hex2StringAspect}从HEX转换成普通字符串
 * @see  Hex2StringAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Hex2StringPointCut {
    
    /**
     * @return
     */
    @AliasFor("names")
    String[] value() default {};
    
    /**
     * 需要转换的参数名，不指定则表示所有String类型的参数
     *
     * @return
     */
    @AliasFor("value")
    String[] names() default {};
}
