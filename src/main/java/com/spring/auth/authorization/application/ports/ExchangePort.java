package com.spring.auth.authorization.application.ports;


import com.spring.auth.exceptions.application.*;
import com.spring.auth.util.TokenUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;

/** @author diegotobalina created on 24/06/2020 */
public interface ExchangePort {
  TokenUtil.JwtWrapper exchange(String token, String clientId)
      throws NotFoundException, UnknownTokenFormatException, InvalidTokenException,
          GeneralSecurityException, IOException, GoogleGetInfoException,
          EmailDoesNotExistsException, LockedUserException, DuplicatedKeyException,
          InfiniteLoopException;
}
