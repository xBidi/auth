package com.spring.server.controller;

import com.spring.server.model.dto.LoginInputDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class) @SpringBootTest public class AuthControllerTest {

    @Autowired private AuthController authController;

    @Test public void contexLoads() {
        assertThat(authController).isNotNull();
    }

    @Test public void login() {
        String username = "username";
        String email = "user@user.com";
        String password = "password";
        LoginInputDto loginInputDto = new LoginInputDto(username, email, password);
        authController.login(loginInputDto);
    }

}
