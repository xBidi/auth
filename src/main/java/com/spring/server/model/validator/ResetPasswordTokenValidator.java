package com.spring.server.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ResetPasswordTokenValidator
    implements ConstraintValidator<ResetPasswordTokenConstraint, String> {

    @Override public void initialize(ResetPasswordTokenConstraint sessionToken) {
    }

    @Override public boolean isValid(String resetPasswordToken, ConstraintValidatorContext cxt) {
        if (resetPasswordToken == null || resetPasswordToken.isEmpty()) {
            return false;
        }
        String regex = "[0-9]{13}-[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
        if (!resetPasswordToken.matches(regex)) {
            return false;
        }
        return true;
    }

}
