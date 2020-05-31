package com.spring.server.model.dto;

import com.spring.server.model.validator.PasswordConstraint;
import com.spring.server.model.validator.UsernameNullableConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;

/**
 * Login endpoint intput dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginInputDto {
  @ApiModelProperty(example = "user")
  @UsernameNullableConstraint
  private String username;

  @ApiModelProperty(example = "user@user.com")
  @Email
  private String email;

  @ApiModelProperty(example = "password")
  @PasswordConstraint
  private String password;
}
