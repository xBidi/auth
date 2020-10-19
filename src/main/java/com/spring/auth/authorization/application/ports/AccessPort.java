package com.spring.auth.authorization.application.ports;

import com.spring.auth.exceptions.application.InvalidTokenException;
import com.spring.auth.exceptions.application.LockedUserException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.util.TokenUtil;

import java.util.List;

/** @author diegotobalina created on 24/06/2020 */
public interface AccessPort {
  TokenUtil.JwtWrapper access(String token, List<String> roleValues, List<String> scopeValues)
      throws NotFoundException, InvalidTokenException, LockedUserException;

  TokenUtil.JwtWrapper access(String userId)
          throws NotFoundException, InvalidTokenException, LockedUserException;
}
