package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * Token dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class TokenDto
    extends SpringTokenDto {
    @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
    private String token;
    @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000") private String expeditionDate;
    @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000") private String expirationDate;
}
