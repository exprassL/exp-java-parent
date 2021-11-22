package com.exp.fluent.config;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class FluentConfig {
    
    @Resource
    private DataSource dataSource;
    
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        以下部分根据实际情况配置，如有mybatis原生文件，可在这里加载
//        bean.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
//        或者加载指定文件
//        bean.setMapperLocations(
//                new ClassPathResource("mapper/xml1.xml"),
//                new ClassPathResource("mapper/xml2.xml")
//        );
        
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLazyLoadingEnabled(true);
        configuration.setMapUnderscoreToCamelCase(true);
        bean.setConfiguration(configuration);
        return bean;
    }
    
    // 定义fluent mybatis的MapperFactory
    @Bean
    public MapperFactory mapperFactory() {
        return new MapperFactory();
    }
}