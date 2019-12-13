package com.spring.demo.filter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Initiating REST filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        LOGGER.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());

        //call next filter in the filter chain
        filterChain.doFilter(request, response);
        // (url/vad, IP-adress/vem, tid/när, valfritt: success/error/result/status-code).
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("Query: " + request.getQueryString());
        System.out.println("IP: " + request.getRemoteAddr());
        System.out.println("Time: "+ Instant.now());
        System.out.println("Type: " + request.getMethod());
        System.out.println("Content type: " + response.getContentType());
        System.out.println("Status: " + response.getStatus());
        System.out.println();


//        LOGGER.info("Logging Response :{}", response.getContentType());
    }

    @Override
    public void destroy() {
    }
}