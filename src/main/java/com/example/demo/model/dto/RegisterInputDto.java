package com.example.demo.model.dto;

import com.example.demo.model.validator.PasswordConstraint;
import com.example.demo.model.validator.UsernameConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Register endpoint intput dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @Getter @Setter @ToString public class RegisterInputDto {
    @ApiModelProperty(example = "user") @UsernameConstraint private String username;
    @ApiModelProperty(example = "email") @Email @NotNull @NotBlank private String email;
    @ApiModelProperty(example = "password") @PasswordConstraint private String password;
}
