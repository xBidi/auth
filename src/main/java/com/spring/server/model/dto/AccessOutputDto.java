package com.spring.server.model.dto;

import com.spring.server.util.TokenUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Access endpoint output dto
 *
 * @author diegotobalina
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccessOutputDto {
  @ApiModelProperty(example = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1")
  private String token;

  @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000")
  private Date issuedAt;

  @ApiModelProperty(example = "2020-02-26T09:48:56.589+0000")
  private Date expiration;

  public AccessOutputDto(final String token, final Date issuedAt, final Date expiration) {
    final String tokenWithPrefix = TokenUtil.addPrefix(token);
    this.token = tokenWithPrefix;
    this.issuedAt = issuedAt;
    this.expiration = expiration;
  }
}
