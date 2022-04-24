package com.exp.web.sso.config;

import java.util.LinkedHashMap;

import com.exp.shiro.ShiroConfig;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SsoShiroFilterConfig extends ShiroConfig {

  @Override
  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = super.shiroFilterFactoryBean(securityManager);

    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    filterChainDefinitionMap.put("/sign/key", "anon");
    filterChainDefinitionMap.put("/sign/in/**", "anon");
    // unauthenticated, unauthorized
    filterChainDefinitionMap.put("/auth/unauth*", "anon");
    filterChainDefinitionMap.put("/**", "authc");
    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    return shiroFilterFactoryBean;
  }

}
