package com.example.demo.security;

import com.example.demo.service.AuthService;
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
@EnableWebSecurity @Slf4j public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired AuthService authService;
    @Value("${server.context.path}") private String contextPath;
    @Value("${server.api.version}") private String apiVersion;
    @Value("${server.path.users}") private String usersPath;
    @Value("${server.path.oauth}") private String authPath;

    @Override protected void configure(HttpSecurity http) throws Exception {
        log.debug("{configure start}");
        http.
            csrf().
            disable().
            sessionManagement().
            sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.
            exceptionHandling().
            authenticationEntryPoint((req, rsp, e) -> {
                String message = e.getMessage();
                rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
            });

        http.antMatcher(String.format("/%s/%s/**", contextPath, apiVersion)).
            authorizeRequests().
            antMatchers(String.format("/%s/%s/%s/**", contextPath, apiVersion, authPath)).
            permitAll().
            antMatchers(HttpMethod.POST,
                String.format("/%s/%s/%s/userInfo", contextPath, apiVersion, authPath)).
            authenticated().
            antMatchers(HttpMethod.PUT,
                String.format("/%s/%s/%s/password/reset", contextPath, apiVersion, usersPath)).
            permitAll().
            antMatchers(HttpMethod.POST,
                String.format("/%s/%s/%s/password/email", contextPath, apiVersion, usersPath)).
            permitAll().
            antMatchers(HttpMethod.POST,
                String.format("/%s/%s/%s/email/verify", contextPath, apiVersion, usersPath)).
            permitAll().
            antMatchers(HttpMethod.POST,
                String.format("/%s/%s/%s/register", contextPath, apiVersion, usersPath)).
            permitAll().
            anyRequest().
            authenticated().
            and().
            addFilterBefore(new AuthenticationFilter(authService),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new GoogleAuthenticationFilter(authService),
                UsernamePasswordAuthenticationFilter.class);
        log.debug("{configure end}");

    }
}
