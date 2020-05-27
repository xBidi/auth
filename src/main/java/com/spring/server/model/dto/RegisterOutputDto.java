package com.spring.server.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Register endpoint output dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @Getter @ToString public class RegisterOutputDto {
    @ApiModelProperty(example = "4b4beaba-42d3-4284-bf7c-4a00100bc828") private String userId;
    @ApiModelProperty(example = "user") private String username;
    @ApiModelProperty(example = "email") private String email;
}
