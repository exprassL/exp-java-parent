package com.exp.web.file.filter;

import com.exp.web.filter.BaseWebFilter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 文件访问过滤器
 */
@Slf4j
@WebFilter(filterName = "fileAccessFilter", urlPatterns = {"/file/access/*"})
public class FileAccessFilter extends BaseWebFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String dir = servletRequest.getParameter("dir"); // FIXME @PathVariable时，此行是否有效？
        log.info("执行过滤器：{}", "fileAccessFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
}
