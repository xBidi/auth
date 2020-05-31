package com.spring.server.service.interfaces;

import com.spring.server.model.dto.*;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface AuthService {

  LoginOutputDto login(final LoginInputDto loginInputDto) throws Exception;

  void logout(final LogoutInputDto logoutInputDto);

  AccessOutputDto access(final AccessInputDto accessInputDto) throws Exception;

  TokenInfoOutputDto tokenInfo(final String token) throws Exception;

  UserInfoOutputDto userInfo(final Principal principal);
}
