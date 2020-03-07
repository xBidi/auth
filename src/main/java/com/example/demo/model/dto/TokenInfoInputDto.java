package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * Token info endpoint input dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class TokenInfoInputDto {
    @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
    private String token;
}
