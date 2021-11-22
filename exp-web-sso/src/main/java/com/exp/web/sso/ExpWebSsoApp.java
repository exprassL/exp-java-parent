package com.exp.web.sso;

import com.exp.fluent.mapper.FileMapper;
import com.exp.web.listener.HttpServletRequestListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.exp"})
@ServletComponentScan(basePackageClasses = {HttpServletRequestListener.class})
@MapperScan(basePackageClasses = {FileMapper.class})
public class ExpWebSsoApp {
    
    public static void main(String[] args) {
        SpringApplication.run(ExpWebSsoApp.class, args);
    }
}
