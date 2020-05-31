package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JwtTokenValidator implements ConstraintValidator<JwtTokenConstraint, String> {

    @Override public void initialize(JwtTokenConstraint jwt) {
    }

    @Override public boolean isValid(String jwt, ConstraintValidatorContext cxt) {
        return RegexUtil.isJwt(jwt);
    }

}
