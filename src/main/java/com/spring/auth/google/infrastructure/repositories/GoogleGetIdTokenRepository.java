package com.spring.auth.google.infrastructure.repositories;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.spring.auth.google.infrastructure.repositories.ports.GoogleGetIdTokenPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/** @author diegotobalina created on 24/06/2020 */
@Slf4j
@Repository
public class GoogleGetIdTokenRepository implements GoogleGetIdTokenPort {

  /**
   * Returns google id_token from a Google refresh token
   *
   * @param refreshToken Refresh token to generate the id_token
   * @param clientId Client id of the refresh token, the id_token will be generated with this
   *     client_id
   * @param clientSecret Client secret of the refresh token, the id_token will be generated with
   *     this client_secret
   * @return Generated id_token from google
   * @throws IOException
   */
  @Override
  public String get(String refreshToken, String clientId, String clientSecret) throws IOException {
    NetHttpTransport netHttpTransport = new NetHttpTransport();
    JacksonFactory jacksonFactory = new JacksonFactory();
    TokenResponse response =
        new GoogleRefreshTokenRequest(
                netHttpTransport, jacksonFactory, refreshToken, clientId, clientSecret)
            .execute();
    return (String) response.get("id_token");
  }
}
