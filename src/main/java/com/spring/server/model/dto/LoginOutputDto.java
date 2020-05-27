package com.spring.server.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Login endpoint output dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @Getter @Setter @ToString public class LoginOutputDto {
    @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
    private String token;
    @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000") private String expeditionDate;
    @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000") private String expirationDate;
}