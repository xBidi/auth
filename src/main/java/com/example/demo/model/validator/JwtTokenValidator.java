package com.example.demo.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JwtTokenValidator implements ConstraintValidator<JwtTokenConstraint, String> {

    @Override public void initialize(JwtTokenConstraint jwt) {
    }

    @Override public boolean isValid(String jwt, ConstraintValidatorContext cxt) {
        if (jwt == null || jwt.isEmpty()) {
            return false;
        }
        String regex = "Bearer [A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
        if (!jwt.matches(regex)) {
            return false;
        }
        return true;
    }

}
