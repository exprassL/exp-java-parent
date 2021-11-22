package com.exp.web.aop.aspect;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.aop.annotation.String2HexPointCut;
import com.exp.web.aop.annotation.String2HexPointCut.FieldString2Hex;
import com.exp.web.exception.WebBizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 切面，用于增强{@link String2HexPointCut}注解的方法，
 * 对注解方法返回值中，由{@link FieldString2Hex}注解的字段执行行{@link StringUtils#str2hex(String)}转换
 */
@Slf4j
@Aspect
@Component
public class String2HexAspect {
    
    
    /**
     * 由{@link String2HexPointCut}注解声明切点
     */
    @Pointcut("@annotation(com.exp.web.aop.annotation.String2HexPointCut)")
    private void pointCut() {}
    
    // Advice须应用到@Override方法上
    @Around(value = "pointCut() && @annotation(annotation)") // TODO 也可以@AfterReturning，将修改后的返回值存入ThreadLocal
    public Object urlEncode(ProceedingJoinPoint joinPoint, String2HexPointCut annotation) throws Throwable {
        Object result = joinPoint.proceed(); // 如果目标方法没有返回值，则此返回null
        if (result == null) {
            return null;
        }
        
        if (result instanceof String) {
            return StringUtils.str2hex((String) result);
        }
        
        List<FieldString2HexTask> plans = new ArrayList<>();
        if (result instanceof List) {
            for (Object item : (List<?>)result) {
                planString2Hex(item, plans);
            }
        } else if (result instanceof Map) {
            for (Map.Entry<?, ?> item : ((Map<?, ?>)result).entrySet()) {
                planString2Hex(item.getValue(), plans);
            }
        } else {
            planString2Hex(result, plans);
        }
        
        for (FieldString2HexTask plan : plans) {
            plan.doTask();
        }
        
        return result;
    }
    
    /**
     * 为指定对象中{@link FieldString2Hex}注解的字段安排转换计划
     *
     * @param item 指定对象
     * @param tasks
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    private void planString2Hex(Object item, List<FieldString2HexTask> tasks) throws IllegalAccessException, UnsupportedEncodingException {
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldString2Hex.class)) {
                field.setAccessible(true);
                Object value = field.get(item);
                if (value == null) {
                    continue;
                }
                
                if (value instanceof String) {
                    tasks.add(FieldString2HexTask.toInstance(field, item, URLEncoder.encode((String) value, "UTF-8")));
                } else {
                    throw WebBizException.produce(
                            "%s注解应该使用在%s类型的成员属性上，实际却使用在了%类型的成员属性上。", // TODO 递归则需允许
                            FieldString2Hex.class.getName(),
                            String.class.getName(),
                            value.getClass().getName());
                }
            }
        }
    }
    
    /**
     * 字段值URL编码任务
     */
    static class FieldString2HexTask {
        
        /**
         * 待编码的字段
         */
        private final Field field;
        
        /**
         * 字段所属对象
         */
        private final Object target;
        
        /**
         * 字段新值
         */
        private final String value;
        
        public FieldString2HexTask(Field field, Object target, String value) {
            this.field = field;
            this.target = target;
            this.value = value;
        }
        
        static FieldString2HexTask toInstance(Field field, Object target, String value) {
            return new FieldString2HexTask(field, target, value);
        }
        
        public void doTask() throws IllegalAccessException {
            field.set(target, value);
        }
    }
}
