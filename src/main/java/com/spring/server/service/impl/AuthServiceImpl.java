package com.spring.server.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.spring.server.model.dto.*;
import com.spring.server.model.entity.SessionToken;
import com.spring.server.model.entity.User;
import com.spring.server.service.GoogleService;
import com.spring.server.service.SessionTokenService;
import com.spring.server.service.interfaces.AuthService;
import com.spring.server.util.PasswordUtil;
import com.spring.server.util.RegexUtil;
import com.spring.server.util.TokenUtil;
import com.spring.server.util.UserUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

/**
 * Authentication functions
 *
 * @author diegotobalina
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  @Autowired private UserServiceImpl userServiceImpl;
  @Autowired private SessionTokenService sessionTokenService;
  @Autowired private GoogleService googleService;

  @Value("${server.auth.secret-key}")
  private String secretKey;

  @Override
  public LoginOutputDto login(final LoginInputDto loginInputDto) throws Exception {
    final String username = loginInputDto.getUsername();
    final String email = loginInputDto.getEmail();
    final String plainPassword = loginInputDto.getPassword();
    final User user = this.userServiceImpl.findByUsernameOrEmail(username, email);
    final String hashedPassword = user.getPassword();
    if (!PasswordUtil.doPasswordsMatch(plainPassword, hashedPassword)) {
      throw new Exception("contraseña incorrecta");
    }
    final SessionToken sessionToken = this.sessionTokenService.generateToken();
    this.userServiceImpl.addSessionToken(user, sessionToken);
    return new LoginOutputDto(sessionToken);
  }

  @Override
  public AccessOutputDto access(final AccessInputDto accessInputDto) throws Exception {
    final String tokenWithPrefix = accessInputDto.getToken();
    final String token = TokenUtil.removePrefix(tokenWithPrefix);
    final SessionToken sessionToken = this.sessionTokenService.findValidByToken(token);
    this.sessionTokenService.refreshToken(token);
    final User user = this.userServiceImpl.findBySessionTokensToken(sessionToken.getToken());
    final String jwt = TokenUtil.generateJwt(user, secretKey);
    final Claims claims = TokenUtil.getClaims(jwt, secretKey);
    final Date issuedAt = claims.getIssuedAt();
    final Date expiration = claims.getExpiration();
    return new AccessOutputDto(jwt, issuedAt, expiration);
  }

  @Override
  public void logout(LogoutInputDto logoutInputDto) {
    String tokenWithPrefix = logoutInputDto.getToken();
    String tokenWithoutPrefix = TokenUtil.removePrefix(tokenWithPrefix);
    sessionTokenService.removeToken(tokenWithoutPrefix);
  }

  @Override
  public TokenInfoOutputDto tokenInfo(String tokenWithPrefix) throws Exception {
    final String tokenWithoutPrefix = TokenUtil.removePrefix(tokenWithPrefix);
    if (RegexUtil.isBasicToken(tokenWithPrefix)) {
      return getSessionTokenInfo(tokenWithoutPrefix);
    }
    if (RegexUtil.isJwt(tokenWithPrefix)) {
      return getJwtTokenInfo(tokenWithoutPrefix, secretKey);
    }
    throw new Exception("unknow jwt format");
  }

  @Override
  public UserInfoOutputDto userInfo(Principal principal) {
    String userId = UserUtil.getUserIdFromPrincipal(principal);
    User user = userServiceImpl.findById(userId);
    return new UserInfoOutputDto(user);
  }

  private TokenInfoOutputDto getSessionTokenInfo(String tokenString) throws Exception {
    final SessionToken sessionToken = sessionTokenService.findValidByToken(tokenString);
    final User user = userServiceImpl.findBySessionTokensToken(sessionToken.getToken());
    return new TokenInfoOutputDto(sessionToken, user.getId());
  }

  private TokenInfoJwtOutputDto getJwtTokenInfo(String jwtWithoutPrefix, String secretKey)
      throws Exception {
    TokenInfoJwtOutputDto tokenInfoOutputDto;
    if ((tokenInfoOutputDto = getJwtInfo(jwtWithoutPrefix, secretKey)) != null) {
      return tokenInfoOutputDto;
    }
    if ((tokenInfoOutputDto = getGoogleJwtInfo(jwtWithoutPrefix)) != null) {
      return tokenInfoOutputDto;
    }
    throw new Exception("Ha ocurrido algo al intentar obtener la información del token");
  }

  private TokenInfoJwtOutputDto getJwtInfo(String jwtWithoutPrefix, String secretKey) {
    try {
      User user = TokenUtil.getUserFromJwt(jwtWithoutPrefix, secretKey);
      Claims claims = TokenUtil.getClaims(jwtWithoutPrefix, secretKey);
      Date issuedAt = claims.getIssuedAt();
      Date expiration = claims.getExpiration();
      return new TokenInfoJwtOutputDto(jwtWithoutPrefix, issuedAt, expiration, user);
    } catch (Exception ex) {
      log.warn(ex.getMessage());
      return null;
    }
  }

  private TokenInfoJwtOutputDto getGoogleJwtInfo(String tokenString) {
    try {
      GoogleIdToken.Payload googleInfo = this.googleService.getGoogleInfo(tokenString);
      if (googleInfo == null) {
        throw new Exception("failed google login");
      }
      Date issuedAt = new Date(googleInfo.getIssuedAtTimeSeconds() * 1000);
      Date expiration = new Date(googleInfo.getExpirationTimeSeconds() * 1000);
      User user = this.googleService.googleLogin(googleInfo);
      return new TokenInfoJwtOutputDto(tokenString, issuedAt, expiration, user);
    } catch (Exception ex) {
      log.warn(ex.getMessage());
      return null;
    }
  }
}
