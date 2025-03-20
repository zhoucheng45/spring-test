package com.example.backend.intercepts;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(
        filterName = "LogFilter",           // 过滤器名称（可选）
        urlPatterns = "/*",                // 拦截所有请求
        initParams = {                     // 初始化参数（可选）
                @WebInitParam(name = "logLevel", value = "debug")
        }
)
public class LogIntercept implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        String logLevel = filterConfig.getInitParameter("logLevel");
        System.out.println("LogFilter initialized with level: " + logLevel);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        System.out.println("Request URI: " + httpRequest.getRequestURI());

        // 必须调用此方法，否则请求会被拦截不继续执行
        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("Response sent for URI: " + httpRequest.getRequestURI());
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
