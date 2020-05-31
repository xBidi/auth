package com.spring.server.filter;

import com.spring.server.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component
@WebFilter("/*")
@Slf4j
public class StatsFilter implements Filter {

  @Override
  public void init(final FilterConfig filterConfig) {
    // empty
  }

  @Override
  public void doFilter(
      final ServletRequest req, final ServletResponse resp, final FilterChain chain)
      throws IOException, ServletException {
    if (!(RequestUtil.isApiRequest(req))) {
      chain.doFilter(req, resp);
      return;
    }
    logRequest(req, resp, chain);
  }

  private void logRequest(
      final ServletRequest req, final ServletResponse resp, final FilterChain chain)
      throws IOException, ServletException {
    long timeBeforeRequest = System.currentTimeMillis();
    chain.doFilter(req, resp);
    long timeAfterRequest = System.currentTimeMillis();
    long totalRequestTime = timeAfterRequest - timeBeforeRequest;
    if (RequestUtil.isApiRequest(req)) {
      log.info("total request time: {} ms", totalRequestTime);
    }
  }

  @Override
  public void destroy() {
    // empty
  }
}
