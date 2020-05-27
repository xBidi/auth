package com.spring.server.security;

import com.spring.server.model.entity.Role;
import com.spring.server.model.entity.User;
import com.spring.server.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
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
@Slf4j public class GoogleAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public GoogleAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

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
        token = token.replace("Bearer ", "");
        try {
            GoogleIdToken.Payload googleInfo = this.authService.getGoogleInfo(token);
            if (googleInfo == null) {
                throw new Exception("google failed login");
            }
            User user = this.authService.googleLogin(googleInfo);
            List<SimpleGrantedAuthority> simpleGrantedAuthorities =
                this.getSimpleGrantedAuthorities(user.getRoles());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getScopes(),
                    simpleGrantedAuthorities);
            SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
            log.debug("authentication ok: {}", user.toString());
        } catch (Exception ex) {
            log.warn("exception: {}", ex.getStackTrace());
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
