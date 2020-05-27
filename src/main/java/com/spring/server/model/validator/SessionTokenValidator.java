package com.spring.server.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SessionTokenValidator implements ConstraintValidator<SessionTokenConstraint, String> {

    @Override public void initialize(SessionTokenConstraint sessionToken) {
    }

    @Override public boolean isValid(String sessionToken, ConstraintValidatorContext cxt) {
        if (sessionToken == null || sessionToken.isEmpty()) {
            return false;
        }
        String regex =
            "Bearer [0-9]{13}-[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
        if (!sessionToken.matches(regex)) {
            return false;
        }
        return true;
    }

}
