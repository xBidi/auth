package com.spring.server.model.dto;

import com.spring.server.model.validator.SessionTokenConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * Access endpoint input dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class AccessInputDto {
    @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
    @SessionTokenConstraint private String token;
}
