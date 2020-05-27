package com.spring.server.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameNullableValidator
    implements ConstraintValidator<UsernameNullableConstraint, String> {

    @Override public void initialize(UsernameNullableConstraint contactNumber) {
    }

    @Override public boolean isValid(String username, ConstraintValidatorContext cxt) {
        if (username == null || username.isEmpty()) {
            return true;
        }
        String regex = "[a-zA-Z0-9\\._\\-]{3,15}";
        if (!username.matches(regex)) {
            return false;
        }
        return true;
    }

}
