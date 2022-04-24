package com.exp.shiro;

import javax.annotation.Resource;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;

/**
 * shiro配置
 */
public class ShiroConfig {

    @Resource
    protected ExpAuthorizingRealm expRealm;

    @Bean
    protected SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(expRealm);
        return securityManager;
    }

    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/auth/unauthenticated"); // 未登录（认证）
        shiroFilterFactoryBean.setUnauthorizedUrl("/auth/unauthorized"); // 未授权
        shiroFilterFactoryBean.setSuccessUrl("/auth/ok");

        return shiroFilterFactoryBean;
    }
}
