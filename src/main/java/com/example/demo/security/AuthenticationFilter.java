package com.example.demo.security;

import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.User;
import com.example.demo.service.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
@Slf4j public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public AuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }
        token = token.replace("Bearer ", "");
        try {
            User user = authService.validateJwt(token);
            List<SimpleGrantedAuthority> simpleGrantedAuthorities =
                this.getSimpleGrantedAuthorities(user.getRoles());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getScopes(),
                    simpleGrantedAuthorities);
            SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
        } catch (JwtException ex) {
            log.warn("invalid jwt");
        } finally {
            chain.doFilter(request, response);
        }


    }

    private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(List<Role> roles) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (Role role : roles) {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getValue()));
        }
        return simpleGrantedAuthorities;
    }

}
