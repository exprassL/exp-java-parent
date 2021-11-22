package com.exp.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid连接池配置
 */
@Configuration
public class DruidConfig {
    
    /**
     * 抛出数据源供DI使用
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }
    
    /**
     * 配置Druid管理后台Servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        
        initParams.put("loginUsername","hq");
        initParams.put("loginPassword","564925080");
        initParams.put("allow","");//默认就是允许所有访问
        initParams.put("deny","192.168.15.21");
        
        bean.setInitParameters(initParams);
        return bean;
    }
    
    
    /**
     * 配置Druid web监控的filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        
        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.css,/druid/*");
        
        bean.setInitParameters(initParams);
        
        bean.setUrlPatterns(Arrays.asList("/*"));
        
        return  bean;
    }
}