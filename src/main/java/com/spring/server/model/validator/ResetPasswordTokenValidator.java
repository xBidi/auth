package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ResetPasswordTokenValidator
    implements ConstraintValidator<ResetPasswordTokenConstraint, String> {

    @Override public void initialize(ResetPasswordTokenConstraint sessionToken) {
    }

    @Override public boolean isValid(String resetPasswordToken, ConstraintValidatorContext cxt) {
        return RegexUtil.isBasicToken(resetPasswordToken);
    }

}
