package com.spring.auth.filters;

import com.spring.auth.log.RequestLogger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.*;

/** @author diegotobalina created on 24/06/2020 */
@Component
@AllArgsConstructor
public class RequestFilter implements Filter {

  private RequestLogger requestLogger;

  @Override
  public void init(FilterConfig filterConfig) {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    requestLogger.log(request, response, chain);
  }

  @Override
  public void destroy() {}
}
