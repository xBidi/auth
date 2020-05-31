package com.spring.server.model.dto;

import com.spring.server.model.validator.VerifyEmailTokenConstraint;
import lombok.Data;

@Data
public class VerifyEmailDto {
  @VerifyEmailTokenConstraint private String token;
}
