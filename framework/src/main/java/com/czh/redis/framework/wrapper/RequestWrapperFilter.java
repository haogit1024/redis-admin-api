package com.czh.redis.framework.wrapper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class RequestWrapperFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("request instanceof RequestWrapper: {}", request instanceof RequestWrapper);
        if (request instanceof RequestWrapper) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
