package com.spring.server.model.dto;

import com.spring.server.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Token info endpoint output dto for jwt
 *
 * @author diegotobalina
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TokenInfoJwtOutputDto extends TokenInfoOutputDto {

  @ApiModelProperty(example = "[ROLE_USER,ROLE_ADMIN]")
  private List<String> roles;

  @ApiModelProperty(example = "[READ,CREATE]")
  private List<String> scopes;

  public TokenInfoJwtOutputDto(String token, Date issuedAt, Date expiration, User user) {
    super(token, issuedAt, expiration, user.getId());
    this.roles = user.getRoles().stream().map(role -> role.getValue()).collect(Collectors.toList());
    this.scopes =
        user.getScopes().stream().map(scope -> scope.getValue()).collect(Collectors.toList());
  }
}
