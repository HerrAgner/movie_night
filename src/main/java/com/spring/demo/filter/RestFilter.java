package com.spring.demo.filter;


import com.spring.demo.db.RestInfoRepository;
import com.spring.demo.entities.RestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RestInfoRepository restInfoRepository;

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.info("Initiating REST filter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        filterChain.doFilter(request, response);

        RestInfo restInfo = new RestInfo(
                request.getRequestURI(),
                request.getQueryString(),
                request.getRemoteAddr(),
                Instant.now().toString(),
                request.getMethod(),
                response.getContentType(),
                response.getStatus());
        restInfoRepository.insert(restInfo);
    }

    @Override
    public void destroy() {
    }
}