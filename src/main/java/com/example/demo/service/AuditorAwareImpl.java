package com.example.demo.service;

import com.example.demo.model.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


public class AuditorAwareImpl implements AuditorAware<String> {
    @Override public Optional<String> getCurrentAuditor() {
        try {
            Object principal =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userId = ((User) principal).getId();
            return Optional.of(userId );
        } catch (Exception e) {
        }
        return Optional.of("anonymousUser");

    }
}
