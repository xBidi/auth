package com.spring.server.security;

import com.spring.server.model.entity.Scope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * Evaluate scope permissions
 *
 * @author diegotobalina
 */
@Component @Slf4j public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override public boolean hasPermission(Authentication authentication, Object accessType,
        Object permission) {
        String requiredPermission = String.valueOf(permission);
        List<Scope> scopes = (List<Scope>) authentication.getCredentials();
        log.debug("required scope: {}, user scopes: {}", requiredPermission, scopes.toString());
        for (Scope scope : scopes) {
            if (requiredPermission.startsWith(scope.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override public boolean hasPermission(Authentication authentication, Serializable serializable,
        String targetType, Object permission) {
        return false;
    }
}
