package com.exp.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * shiro配置
 */
@Component
public class ShiroConfig {

    @Resource
    public ExpAuthorizingRealm expRealm;
    
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(expRealm);
        return securityManager;
    }
    
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/unauthenticated"); // 未登录（认证）
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized"); // 未授权
        shiroFilterFactoryBean.setSuccessUrl("/");
    
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/", "authc");
        filterChainDefinitionMap.put("/unauthenticated", "anon");
        filterChainDefinitionMap.put("/sso/sign/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    
        return shiroFilterFactoryBean;
    }
}
