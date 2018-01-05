package com.ctacorp.syndication.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class MultiReadServletFilter implements Filter {

    private static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
        // Enable Multi-Read for PUT and POST requests
        add("PUT");
        add("POST");
    }};

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            // Check wether the current request needs to be able to support the body to be read multiple times
            if(MULTI_READ_HTTP_METHODS.contains(request.getMethod())) {
                // Override current HttpServletRequest with custom implementation
                filterChain.doFilter(new MultiReadHttpServletRequest(request), servletResponse);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}