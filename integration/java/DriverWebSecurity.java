package com.spring.server.security;

import lombok.extern.slf4j.Slf4j;
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
@EnableWebSecurity @Slf4j public class DriverWebSecurity extends WebSecurityConfigurerAdapter {


    @Override protected void configure(HttpSecurity http) throws Exception {
        log.info("starting configuration");
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

        DriverAuthenticationFilter driverAuthenticationFilter = new DriverAuthenticationFilter();

        http.antMatcher("/api/**").
            authorizeRequests().
            anyRequest().authenticated().and().
            addFilterBefore(driverAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
