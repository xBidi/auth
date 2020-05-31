package com.spring.server.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * Token info endpoint generic output dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class TokenInfoOutputDto {

    private String token;
    private String expeditionDate;
    private String expirationDate;
    private String userid;
}
