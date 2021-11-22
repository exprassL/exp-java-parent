package com.exp.web.aop.aspect;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.aop.annotation.Hex2StringPointCut;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 切面，用于增强{@link Hex2StringPointCut}注解的方法，将由注解属性{@link Hex2StringPointCut#names()}声明的方法参数从HEX转为普通字符串
 */
@Slf4j
@Aspect
@Component
public class Hex2StringAspect {
    
    /**
     * 由{@link Hex2StringPointCut}注解声明切点
     */
    @Pointcut("@annotation(com.exp.web.aop.annotation.Hex2StringPointCut)")
    private void pointCut() {}
    
    /**
     * 增强处理方法
     * TODO 当使用在Controler上，且Controller参数为u@RequestParam时，Aspect修改的参数未能传递到Controller层？
     *
     * @param joinPoint 当前切点
     * @param annotation 声明切点的注解
     */
    @Around(value = "pointCut() && @annotation(annotation)")
    public Object hex2String(ProceedingJoinPoint joinPoint, Hex2StringPointCut annotation) throws Throwable {
        Object[] args = joinPoint.getArgs(); // PointCut处目标方法参数列表
        if (args.length == 0) {
            log.info("目标方法参数列表为空");
            return joinPoint.proceed(args);
        }
        
        String[] names = annotation.names(); // 注解声明的要转换的参数名
        if (names.length == 0) {
            // 未声明时表示转换所有参数中的String类型参数
            log.info("遍历参数列表以转换其中的String类型参数");
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof String) {
                    args[i] = StringUtils.hex2Str(((String) args[i]).replaceAll("u", "\\\\u"));
                }
            }
        } else {
            String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
    
            log.info("遍历转换注解声明名称的参数");
            List<String> parameterNameList = Arrays.asList(parameterNames);
            for (String name : names) {
                log.info("当前待转换的参数名：{}", name);
                int i = parameterNameList.indexOf(name);
                // 以下可能抛出类型转换异常：指定的参数不是String类型；可能抛出数组下标越界异常：指定的参数不在参数列表，i=-1？TODO 验证
                args[i] = StringUtils.hex2Str   (((String) args[i]).replaceAll("u", "\\\\"));
            }
        }
        return joinPoint.proceed(args);
    }
}
