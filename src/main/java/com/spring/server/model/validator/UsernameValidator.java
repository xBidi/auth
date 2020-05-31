package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {

    @Override public void initialize(UsernameConstraint contactNumber) {
    }

    @Override public boolean isValid(String username, ConstraintValidatorContext cxt) {
        if (StringUtils.isBlank(username))
            return false;
        return RegexUtil.isValidUsername(username);
    }

}
