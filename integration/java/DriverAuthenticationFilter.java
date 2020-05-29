package com.spring.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.server.model.dto.TokenInfoJwtOutputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Authentication filter
 *
 * @author diegotobalina
 */
@Slf4j public class DriverAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            log.debug("no token");
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.debug("already authenticated");
            chain.doFilter(request, response);
            return;
        }
        try {
            Driver driver = new Driver("http://localhost:8080/api/v1");
            String tokenInfo = driver.userInfo(token);
            ObjectMapper objectMapper = new ObjectMapper();
            TokenInfoJwtOutputDto tokenInfoJwtOutputDto =
                objectMapper.readValue(tokenInfo, TokenInfoJwtOutputDto.class);
            List<SimpleGrantedAuthority> simpleGrantedAuthorities =
                this.getSimpleGrantedAuthorities(tokenInfoJwtOutputDto.getRoles());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(tokenInfoJwtOutputDto.getUserid(),
                    tokenInfoJwtOutputDto.getScopes(), simpleGrantedAuthorities);
            SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
            log.debug("authentication ok: {}", tokenInfoJwtOutputDto.getUserid());
        } catch (Exception ex) {
            log.warn("exception: {}", ex.getStackTrace());
        } finally {
            chain.doFilter(request, response);
        }

    }

    private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<String> roles) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        return simpleGrantedAuthorities;
    }

}
