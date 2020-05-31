package com.spring.server.model.dto;

import com.spring.server.model.entity.SessionToken;
import com.spring.server.util.TokenUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * Login endpoint output dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class LoginOutputDto {
    @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
    private String token;
    @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000") private Date issuedAt;
    @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000") private Date expiration;

    public LoginOutputDto(SessionToken sessionToken) {
        String tokenWithPrefix = TokenUtil.addPrefix(sessionToken.getToken());
        this.setToken(tokenWithPrefix);
        this.setIssuedAt(sessionToken.getIssuedAt());
        this.setExpiration(sessionToken.getExpiration());
    }
}
