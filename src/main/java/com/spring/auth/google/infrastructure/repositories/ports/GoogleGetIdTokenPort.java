package com.spring.auth.google.infrastructure.repositories.ports;

import com.spring.auth.exceptions.application.GoogleGetInfoException;

import java.io.IOException;
import java.security.GeneralSecurityException;

/** @author diegotobalina created on 24/06/2020 */
public interface GoogleGetIdTokenPort {
  String get(String refreshToken, String clientId, String clientSecret)
      throws GeneralSecurityException, IOException, GoogleGetInfoException;
}
