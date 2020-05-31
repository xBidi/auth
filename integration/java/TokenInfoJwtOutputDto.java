package com.spring.server.security;

import com.spring.server.model.dto.TokenInfoOutputDto;
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

    private List<String> roles;
    private List<String> scopes;

    public TokenInfoJwtOutputDto(String token, String expeditionDate, String expirationDate,
        String userid, List<String> roles, List<String> scopes) {
        super(token, expeditionDate, expirationDate, userid);
        this.roles = roles;
        this.scopes = scopes;
    }
}
