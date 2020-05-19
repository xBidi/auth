package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Register endpoint intput dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @Getter @Setter @ToString public class RegisterInputDto {
    @ApiModelProperty(example = "user") private String username;
    @ApiModelProperty(example = "email") private String email;
    @ApiModelProperty(example = "password") private String password;
}
