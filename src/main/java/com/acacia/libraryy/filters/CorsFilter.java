package com.acacia.libraryy.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，可以用于加载配置信息
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 设置允许跨域的来源
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 设置允许的 HTTP 方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 设置允许的请求头
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // 设置预检请求的缓存时间（单位：秒）
        response.setHeader("Access-Control-Max-Age", "3600");

        // 如果是 OPTIONS 请求，直接返回 HTTP 200 OK
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 继续执行后续的过滤器或 Servlet
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // 销毁方法，用于清理资源
    }
}