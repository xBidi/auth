package com.example.demo.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override public void initialize(PasswordConstraint contactNumber) {
    }

    @Override public boolean isValid(String password, ConstraintValidatorContext cxt) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        if (password.length() < 6 || password.length() > 254) {
            return false;
        }
        return true;
    }

}
