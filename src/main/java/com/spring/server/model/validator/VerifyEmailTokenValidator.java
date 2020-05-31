package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VerifyEmailTokenValidator
    implements ConstraintValidator<VerifyEmailTokenConstraint, String> {

    @Override public void initialize(VerifyEmailTokenConstraint sessionToken) {
    }

    @Override public boolean isValid(String verifyEmailToken, ConstraintValidatorContext cxt) {
        return RegexUtil.isBasicToken(verifyEmailToken);
    }

}
