package com.spring.server.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * Token info endpoint output dto for jwt
 *
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class TokenInfoJwtOutputDto
    extends TokenInfoOutputDto {

    @ApiModelProperty(example = "[ROLE_USER,ROLE_ADMIN]")private List<String> roles;
    @ApiModelProperty(example = "[READ,CREATE]")private List<String> scopes;

    public TokenInfoJwtOutputDto(String token, String expeditionDate, String expirationDate,
        String userid, List<String> roles, List<String> scopes) {
        super(token, expeditionDate, expirationDate, userid);
        this.roles = roles;
        this.scopes = scopes;
    }
}
