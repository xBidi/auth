package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter public class RegisterInputDto {
    @ApiModelProperty(example = "user") private String username;
    @ApiModelProperty(example = "password") private String password;
}
