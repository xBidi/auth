package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SessionTokenValidator implements ConstraintValidator<SessionTokenConstraint, String> {

  @Override
  public void initialize(SessionTokenConstraint sessionToken) {}

  @Override
  public boolean isValid(String sessionToken, ConstraintValidatorContext cxt) {
    return RegexUtil.isBasicToken(sessionToken);
  }
}
