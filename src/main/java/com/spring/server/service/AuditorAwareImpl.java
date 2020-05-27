package com.spring.server.service;

import com.spring.server.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j public class AuditorAwareImpl implements AuditorAware<String> {
    @Override public Optional<String> getCurrentAuditor() {
        try {
            Object principal =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User) {
                String userId = ((User) principal).getId();
                return Optional.of(userId);
            }
            return Optional.of(principal.toString());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return Optional.of("anonymousUser");

    }
}
