package com.spring.server.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.spring.server.model.entity.User;
import com.spring.server.service.GoogleService;
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
@Slf4j public class GoogleAuthenticationFilter extends OncePerRequestFilter {

    private final GoogleService googleService;

    public GoogleAuthenticationFilter(GoogleService googleService) {
        this.googleService = googleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {
        var authenticationUtil = new AuthenticationUtil(request, response, chain).invoke();
        if (!authenticationUtil.isValid()) {
            return;
        }
        try {
            final String jwtWithPrefix = authenticationUtil.getJwtWithPrefix();
            final String jwtWithoutPrefix = TokenUtil.removePrefix(jwtWithPrefix);
            GoogleIdToken.Payload googleInfo = this.googleService.getGoogleInfo(jwtWithoutPrefix);
            if (googleInfo == null) {
                throw new Exception("google failed login");
            }
            User user = this.googleService.googleLogin(googleInfo);
            authenticationUtil.authenticate(user);
        } catch (Exception ex) {
            log.warn("exception: {}", ex.getStackTrace());
        } finally {
            chain.doFilter(request, response);
        }
    }
}
