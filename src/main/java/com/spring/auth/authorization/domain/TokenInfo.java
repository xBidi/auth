package com.spring.auth.authorization.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@ToString
public class TokenInfo {

  private String token;
  private Date issuedAt;
  private Date expiration;
  private String userId;
  private List<String> roles;
  private List<String> scopes;

  public TokenInfo(String token, Date issuedAt, Date expiration, String userId) {
    this.token = token;
    this.issuedAt = issuedAt;
    this.expiration = expiration;
    this.userId = userId;
  }

  public TokenInfo(
      String token,
      Date issuedAt,
      Date expiration,
      String userId,
      List<String> roles,
      List<String> scopes) {
    this.token = token;
    this.issuedAt = issuedAt;
    this.expiration = expiration;
    this.userId = userId;
    this.roles = roles;
    this.scopes = scopes;
  }
}
