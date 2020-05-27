package com.spring.server.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VerifyEmailTokenValidator
    implements ConstraintValidator<VerifyEmailTokenConstraint, String> {

    @Override public void initialize(VerifyEmailTokenConstraint sessionToken) {
    }

    @Override public boolean isValid(String verifyEmailToken, ConstraintValidatorContext cxt) {
        if (verifyEmailToken == null || verifyEmailToken.isEmpty()) {
            return false;
        }
        String regex = "[0-9]{13}-[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
        if (!verifyEmailToken.matches(regex)) {
            return false;
        }
        return true;
    }

}
