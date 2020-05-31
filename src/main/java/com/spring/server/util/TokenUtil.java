package com.spring.server.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.server.model.entity.Role;
import com.spring.server.model.entity.Scope;
import com.spring.server.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class TokenUtil {

  private static final String PREFIX = "Bearer ";

  public static String addPrefix(String token) {
    if (StringUtils.startsWith(token, PREFIX)) return token;
    return PREFIX + token;
  }

  public static String removePrefix(String token) {
    if (!StringUtils.startsWith(token, PREFIX)) return token;
    return token.replace(PREFIX, "");
  }

  public static Map<String, Object> generateJwtClaims(
      final String userId, final List<Scope> scopes, final List<Role> roles) {
    return new HashMap<>() {
      {
        put("user", userId);
        put("scopes", scopes);
        put("roles", roles);
      }
    };
  }

  public static Map<String, Object> generateJwtClaims(final User user) {
    String userId = user.getId();
    List<Scope> scopes = user.getScopes();
    List<Role> roles = user.getRoles();
    return generateJwtClaims(userId, scopes, roles);
  }

  public static String generateJwt(final User user, final String secretKey) {
    final Map<String, Object> claims = TokenUtil.generateJwtClaims(user);
    final String userId = user.getId();
    final Date issuedDate = new Date(System.currentTimeMillis());
    final long expirationTime = 1000 * 60 * 15; // 1s > 1m > 15m
    final Date expirationDate = new Date(issuedDate.getTime() + expirationTime);
    byte[] secretKeyBytes = secretKey.getBytes();
    return TokenUtil.generateJwt(userId, claims, issuedDate, expirationDate, secretKeyBytes);
  }

  public static String generateJwt(
      final String userId,
      final Map<String, Object> claims,
      final Date issuedDate,
      final Date expirationDate,
      final byte[] key) {
    final String jwtId = UUID.randomUUID().toString();
    return Jwts.builder()
        .setId(jwtId)
        .setSubject(userId)
        .setClaims(claims)
        .setIssuedAt(issuedDate)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();
  }

  public static Claims getClaims(final String token, final String secretKey) {
    return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
  }

  public static List<Role> getRoles(final Claims claims) {
    final ObjectMapper mapper = new ObjectMapper();
    final Object roles = claims.get("roles");
    return mapper.convertValue(roles, new TypeReference<List<Role>>() {});
  }

  public static List<Scope> getScopes(final Claims claims) {
    final ObjectMapper mapper = new ObjectMapper();
    final Object scopes = claims.get("scopes");
    return mapper.convertValue(scopes, new TypeReference<List<Scope>>() {});
  }

  public static String getUserId(final Claims claims) {
    final Object userId = claims.get("user");
    return userId.toString();
  }

  public static User getUserFromJwt(String jwt, String secretKey) throws Exception {
    String jwtWithoutPrefix = TokenUtil.removePrefix(jwt);
    Claims claims = TokenUtil.getClaims(jwtWithoutPrefix, secretKey);
    String userId = TokenUtil.getUserId(claims);
    List<Role> roles = TokenUtil.getRoles(claims);
    List<Scope> scopes = TokenUtil.getScopes(claims);
    return new User(userId, roles, scopes);
  }
}
