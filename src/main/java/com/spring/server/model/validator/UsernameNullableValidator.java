package com.spring.server.model.validator;

import com.spring.server.util.RegexUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameNullableValidator
    implements ConstraintValidator<UsernameNullableConstraint, String> {

  @Override
  public void initialize(UsernameNullableConstraint contactNumber) {}

  @Override
  public boolean isValid(String username, ConstraintValidatorContext cxt) {
    if (StringUtils.isBlank(username)) return true;
    return RegexUtil.isValidUsername(username);
  }
}
