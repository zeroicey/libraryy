package com.acacia.libraryy.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MockSuccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Allow /api/auth/login and /api/auth/register to pass through
        if ("/api/auth/login".equals(path) || "/api/auth/register".equals(path)) {
            chain.doFilter(req, res);
            return;
        }

        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method) || 
            "POST".equalsIgnoreCase(method) || 
            "PUT".equalsIgnoreCase(method) || 
            "DELETE".equalsIgnoreCase(method)) {
            
            // For OPTIONS preflight requests, it's common to also return 200 OK.
            // The existing CorsFilter already handles OPTIONS, but if this filter is placed before it,
            // or if CorsFilter is removed/modified, this check might be useful.
            if ("OPTIONS".equalsIgnoreCase(method)) {
                 response.setStatus(HttpServletResponse.SC_OK);
                 // Optionally set CORS headers here if this filter is meant to replace CorsFilter for these paths
                 // response.setHeader("Access-Control-Allow-Origin", "*");
                 // response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                 // response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
                 // response.setHeader("Access-Control-Max-Age", "3600");
                return; // Do not proceed further for OPTIONS if handled here
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"Request received successfully (mocked)\"}");
            // Do not call chain.doFilter(req, res) to stop further processing
            return; 
        }

        // For other methods or if the condition is not met, continue the chain.
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}