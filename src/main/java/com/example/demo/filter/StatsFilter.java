package com.example.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component @WebFilter("/*") @Slf4j public class StatsFilter implements Filter {


    @Override public void init(FilterConfig filterConfig) throws ServletException {
        // empty
    }

    @Override public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
        throws IOException, ServletException {
        long time = System.currentTimeMillis();
        try {
            chain.doFilter(req, resp);
        } finally {
            time = System.currentTimeMillis() - time;
            log.debug("total request time: {} ms", time);
        }
    }

    @Override public void destroy() {
        // empty
    }
}
