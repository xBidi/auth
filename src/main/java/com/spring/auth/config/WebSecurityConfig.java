package com.spring.auth.config;

import com.spring.auth.exceptions.domain.ErrorResponse;
import com.spring.auth.exceptions.util.ExceptionUtil;
import com.spring.auth.filters.auth.BearerAuthenticationFilter;
import com.spring.auth.filters.auth.GoogleAuthenticationFilter;
import com.spring.auth.google.application.ports.GoogleLoginPort;
import com.spring.auth.google.infrastructure.repositories.ports.GoogleGetInfoPort;
import com.spring.auth.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author diegotobalina created on 24/06/2020
 */
@Slf4j
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${server.auth.secret-key}")
    private String secretKey;

    @Value("${api.prefix}")
    private String apiPrefix;

    private GoogleGetInfoPort googleGetInfoPort;
    private GoogleLoginPort googleLoginPort;

    public WebSecurityConfig(GoogleGetInfoPort googleGetInfoPort, GoogleLoginPort googleLoginPort) {
        this.googleGetInfoPort = googleGetInfoPort;
        this.googleLoginPort = googleLoginPort;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(this::exceptionDuringAuthorizationProcess);

        // custom authentication filters
        var bearerAuthFilter = new BearerAuthenticationFilter(secretKey);
        var googleAuthFilter = new GoogleAuthenticationFilter(googleGetInfoPort, googleLoginPort);

        String apiContext = String.format("%s/**", apiPrefix);
        String accessEndpoint = String.format("%s/oauth2/access", apiContext);
        String loginEndpoint = String.format("%s/oauth2/login", apiContext);
        String logoutEndpoint = String.format("%s/oauth2/logout", apiContext);
        String tokenInfoEndpoint = String.format("%s/oauth2/tokenInfo", apiContext);
        String exchangeEndpoint = String.format("%s/oauth2/exchange", apiContext);
        String cabllbackEndpoint = String.format("%s/oauth2/authorize", apiContext);
        String authorizeUserInfo = String.format("%s/oauth2/authorize/userInfo", apiContext);
        String registerEndpoint = String.format("%s/users", apiContext); // register endpoint

        http.antMatcher(apiContext)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, accessEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.GET, authorizeUserInfo)
                .permitAll()
                .antMatchers(HttpMethod.POST, cabllbackEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.POST, loginEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.DELETE, logoutEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.GET, tokenInfoEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.GET, exchangeEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.POST, registerEndpoint)
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(bearerAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(googleAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private void exceptionDuringAuthorizationProcess(
            HttpServletRequest req, HttpServletResponse rsp, AuthenticationException e)
            throws IOException {
        log.info("UNAUTHORIZED {} {}", req.getMethod(), req.getRequestURI());
        ErrorResponse errorResponse = ExceptionUtil.getErrorResponse(req, HttpStatus.UNAUTHORIZED, e);
        RequestUtil.returnErrorResponse(rsp, errorResponse);
    }
}
