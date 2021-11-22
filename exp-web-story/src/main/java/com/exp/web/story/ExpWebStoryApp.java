package com.exp.web.story;

import com.exp.fluent.mapper.FileMapper;
import com.exp.web.listener.HttpServletRequestListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.exp")
@ServletComponentScan(basePackageClasses = {HttpServletRequestListener.class})
@MapperScan(basePackageClasses = FileMapper.class)
public class ExpWebStoryApp {
    public static void main(String[] args) {
        SpringApplication.run(ExpWebStoryApp.class, args);
    }
}
