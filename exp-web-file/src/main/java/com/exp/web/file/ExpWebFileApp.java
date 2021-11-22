package com.exp.web.file;

import com.exp.web.file.filter.FileAccessFilter;
import com.exp.web.listener.HttpServletRequestListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.exp"})
@ServletComponentScan(basePackageClasses = {HttpServletRequestListener.class, FileAccessFilter.class})
public class ExpWebFileApp {
    
    public static void main(String[] args) {
        SpringApplication.run(ExpWebFileApp.class, args);
    }
}
