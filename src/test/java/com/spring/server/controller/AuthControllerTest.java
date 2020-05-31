package com.spring.server.controller;

import com.spring.server.model.dto.*;
import com.spring.server.service.interfaces.AuthService;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

  @InjectMocks private AuthController authController;
  @Mock private AuthService authService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void contexLoads() {
    assertThat(authController).isNotNull();
  }

  @Test
  public void login() throws Exception {
    String username = "username";
    String email = "user@user.com";
    String password = "password";
    var input = new LoginInputDto(username, email, password);
    Mockito.when(this.authService.login(Mockito.any())).thenReturn(new LoginOutputDto());
    var output = authController.login(input);
    Assert.notNull(output);
  }

  @Test
  public void logout() throws Exception {
    String token = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1";
    var input = new LogoutInputDto(token);
    Mockito.doNothing().when(this.authService).logout(Mockito.any());
    authController.logout(input);
    Assert.isTrue(true);
  }

  @Test
  public void access() throws Exception {
    String token = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1";
    var input = new AccessInputDto(token);
    Mockito.when(authService.access(Mockito.any())).thenReturn(new AccessOutputDto());
    var output = authController.access(input);
    Assert.notNull(output);
  }

  @Test
  public void tokenInfo() throws Exception {
    String token = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1";
    Mockito.when(authService.tokenInfo(Mockito.any())).thenReturn(new TokenInfoOutputDto());
    var output = authController.tokenInfo(token);
    Assert.notNull(output);
  }

  @Test
  public void userInfo() throws Exception {
    String token = "Bearer 1582711356070-5e85f5f2-a2bc-4e08-b0ff-7a4aa55951c1";
    Mockito.when(authService.userInfo(Mockito.any())).thenReturn(new UserInfoOutputDto());
    var output = authController.userInfo(token, null);
    Assert.notNull(output);
  }
}
