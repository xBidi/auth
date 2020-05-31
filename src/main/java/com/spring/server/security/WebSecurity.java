package com.spring.server.security;

import com.spring.server.service.GoogleService;
import com.spring.server.service.impl.AuthServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Configure authorized paths
 *
 * @author diegotobalina
 */
@EnableWebSecurity
@Slf4j
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Autowired AuthServiceImpl authServiceImpl;
  @Autowired GoogleService googleService;

  @Value("${server.auth.secret-key}")
  private String secretKey;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    log.info("starting configuration");
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.exceptionHandling()
        .authenticationEntryPoint(
            (req, rsp, e) -> {
              String message = e.getMessage();
              rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
            });

    AuthenticationFilter aFilter = new AuthenticationFilter(secretKey);
    GoogleAuthenticationFilter gFilter = new GoogleAuthenticationFilter(googleService);

    http.antMatcher("/api/v1/**")
        .authorizeRequests()
        .antMatchers("/api/v1/oauth2/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/v1/oauth2/userInfo")
        .authenticated()
        .antMatchers(HttpMethod.PUT, "/api/v1/users/password/reset")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/users/password/email")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/users/email/verify")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/api/v1/users/register")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(aFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(gFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
