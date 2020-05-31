package com.spring.server.security;

import com.spring.server.model.entity.User;
import com.spring.server.util.AuthenticationUtil;
import com.spring.server.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication filter
 *
 * @author diegotobalina
 */
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

  private final String secretKey;

  public AuthenticationFilter(String secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    var authenticationUtil = new AuthenticationUtil(request, response, chain).invoke();
    if (!authenticationUtil.isValid()) {
      return;
    }
    try {
      final String jwtWithPrefix = authenticationUtil.getJwtWithPrefix();
      final String jwtWithoutPrefix = TokenUtil.removePrefix(jwtWithPrefix);
      User user = TokenUtil.getUserFromJwt(jwtWithoutPrefix, secretKey);
      authenticationUtil.authenticate(user);
    } catch (Exception ex) {
      log.warn("exception: {}", ex.getStackTrace());
    } finally {
      chain.doFilter(request, response);
    }
  }
}
