package com.spring.server.model.dto;

import com.spring.server.model.entity.SessionToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * Token info endpoint generic output dto
 *
 * @author diegotobalina
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TokenInfoOutputDto {
  @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
  private String token;

  @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000")
  private Date issuedAt;

  @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000")
  private Date expiration;

  @ApiModelProperty(example = "4b4beaba-42d3-4284-bf7c-4a00100bc828")
  private String userId;

  public TokenInfoOutputDto(SessionToken sessionToken, String userId) {
    this.setToken(sessionToken.getToken());
    this.setIssuedAt(sessionToken.getIssuedAt());
    this.setExpiration(sessionToken.getExpiration());
    this.setUserId(userId);
  }
}
