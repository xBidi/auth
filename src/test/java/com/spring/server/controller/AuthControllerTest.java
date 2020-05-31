package com.spring.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.server.model.dto.LoginInputDto;
import com.spring.server.model.dto.LoginOutputDto;
import com.spring.server.service.interfaces.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.Application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class) @ContextConfiguration(classes = Application.class)
@WebMvcTest(controllers = AuthController.class) public class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @InjectMocks private AuthController authController;
    @Mock AuthService authService;
    private ObjectMapper objectMapper = new ObjectMapper();


    @Test public void login() throws Exception {
        Mockito.when(authService.login(Mockito.any())).thenReturn(new LoginOutputDto());
        LoginInputDto input = new LoginInputDto("user", "user@user.com", "password");
        mockMvc.perform(post("/api/v1/oauth2/login").contentType("application/json")
            .param("sendWelcomeMail", "true").content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk());
    }

    @Test public void logout() {
    }

    @Test public void access() {
    }

    @Test public void tokenInfo() {
    }

    @Test public void userInfo() {
    }

}
