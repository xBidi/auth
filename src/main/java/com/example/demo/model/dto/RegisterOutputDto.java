package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Register endpoint output dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @Getter public class RegisterOutputDto {
    @ApiModelProperty(example = "4b4beaba-42d3-4284-bf7c-4a00100bc828") private String userId;
    @ApiModelProperty(example = "user") private String username;
}
