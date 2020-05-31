package com.spring.server.model.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override public void initialize(PasswordConstraint contactNumber) {
    }

    @Override public boolean isValid(String password, ConstraintValidatorContext cxt) {
        if (StringUtils.isBlank(password))
            return false;
        return (password.length() > 6 && password.length() < 254);
    }

}
