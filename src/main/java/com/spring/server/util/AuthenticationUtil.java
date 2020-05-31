package com.spring.server.util;

import com.spring.server.model.entity.Role;
import com.spring.server.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AuthenticationUtil {

  private boolean myResult;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain chain;
  private String jwtWithPrefix;

  public AuthenticationUtil(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
    this.request = request;
    this.response = response;
    this.chain = chain;
  }

  public boolean isValid() {
    return myResult;
  }

  public String getJwtWithPrefix() {
    return jwtWithPrefix;
  }

  public AuthenticationUtil invoke() throws IOException, ServletException {
    jwtWithPrefix = request.getHeader("Authorization");
    if (!RegexUtil.isJwt(jwtWithPrefix)) {
      log.debug("empty or malformed token");
      chain.doFilter(request, response);
      myResult = false;
      return this;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      log.debug("already authenticated");
      chain.doFilter(request, response);
      myResult = false;
      return this;
    }
    myResult = true;
    return this;
  }

  public void authenticate(User user) {
    List<SimpleGrantedAuthority> simpleGrantedAuthorities =
        this.getSimpleGrantedAuthorities(user.getRoles());
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(user, user.getScopes(), simpleGrantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    log.debug("authentication ok: {}", user.toString());
  }

  private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<Role> roles) {
    List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
    for (Role role : roles) {
      simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getValue()));
    }
    return simpleGrantedAuthorities;
  }
}
