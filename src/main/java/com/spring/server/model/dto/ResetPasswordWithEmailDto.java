package com.spring.server.model.dto;

import com.spring.server.model.validator.PasswordConstraint;
import com.spring.server.model.validator.ResetPasswordTokenConstraint;
import lombok.Data;

@Data
public class ResetPasswordWithEmailDto {

  @ResetPasswordTokenConstraint private String token;
  @PasswordConstraint private String newPassword;
}
