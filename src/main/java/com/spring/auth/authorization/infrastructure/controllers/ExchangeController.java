package com.spring.auth.authorization.infrastructure.controllers;

import com.spring.auth.anotations.components.controllers.AuthorizationController;
import com.spring.auth.authorization.application.ports.ExchangePort;
import com.spring.auth.authorization.infrastructure.dto.output.AccessOutputDto;
import com.spring.auth.exceptions.application.*;
import com.spring.auth.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.security.GeneralSecurityException;

/** @author diegotobalina created on 24/06/2020 */
@Slf4j
@Validated
@AllArgsConstructor
@AuthorizationController
public class ExchangeController {

  private ExchangePort exchangePort;

  @ApiOperation(
      value = "Exchange token",
      notes = "Cambia un token de google por un token propio del server")
  @GetMapping("/exchange")
  public AccessOutputDto tokenInfo(
      @RequestParam(value = "client_id", required = false) String clientId,
      @RequestParam @NotEmpty final String token) // todo: validate param
      throws NotFoundException, UnknownTokenFormatException, InvalidTokenException,
          GeneralSecurityException, IOException, GoogleGetInfoException,
          EmailDoesNotExistsException, LockedUserException, DuplicatedKeyException,
          InfiniteLoopException {
    final TokenUtil.JwtWrapper access = exchangePort.exchange(token, clientId);
    return new AccessOutputDto(access);
  }
}
