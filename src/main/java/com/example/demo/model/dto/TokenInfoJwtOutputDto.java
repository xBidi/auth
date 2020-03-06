package com.example.demo.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter public class TokenInfoJwtOutputDto
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
