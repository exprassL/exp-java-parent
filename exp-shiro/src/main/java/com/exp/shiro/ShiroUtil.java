package com.exp.shiro;

import com.exp.fluent.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * 访问Principal等shiro内置对象。
 */
public final class ShiroUtil {
    
    /**
     * @return 返回当前登录用户
     */
    public static User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}
