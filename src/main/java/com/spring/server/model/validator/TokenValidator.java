package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;
import com.spring.server.util.TokenUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TokenValidator implements ConstraintValidator<TokenConstraint, String> {

    @Override public void initialize(TokenConstraint token) {
    }

    @Override public boolean isValid(String token, ConstraintValidatorContext cxt) {
        return RegexUtil.isJwt(token) || RegexUtil.isBasicToken(token);
    }

}
