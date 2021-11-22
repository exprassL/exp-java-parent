package com.exp.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.filter.OrderedFilter;

import javax.servlet.*;

@Slf4j
public abstract class BaseWebFilter implements OrderedFilter {

    private String filterName;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterName = filterConfig.getFilterName();
        log.info("过滤器已启动：{}", filterName);
    }

    @Override
    public void destroy() {
        log.info("过滤器已销毁：{}", filterName);
    }
}
