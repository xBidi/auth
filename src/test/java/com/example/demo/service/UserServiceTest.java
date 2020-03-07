package com.example.demo.service;

import com.example.demo.model.dto.RegisterInputDto;
import com.example.demo.model.dto.RegisterOutputDto;
import com.example.demo.model.dto.UserInfoOutputDto;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.Token;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * User service tests
 *
 * @author diegotobalina
 */
@SpringBootTest public class UserServiceTest {

    @InjectMocks UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private RoleService roleService;
    @Mock private ScopeService scopeService;
    @Mock private AuthService authService;


    @BeforeEach public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test public void findByIdOk() throws Exception {
        String userId = "userid";
        User expectedUser = new User();
        expectedUser.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        User user = this.userService.findById(userId);
        assertEquals(expectedUser, user);
    }

    @Test public void findByIdException() throws Exception {
        String userId = "userid";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        try {
            this.userService.findById(userId);
        } catch (Exception e) {
            if (e.getMessage().equals("Unknown user with id: " + userId)) {
                assertTrue(true);
            } else {
                throw e;
            }
        }
    }

    @Test public void findByUsernameOk() throws Exception {
        String username = "username";
        User expectedUser = new User();
        expectedUser.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));
        User user = this.userService.findByUsername(username);
        assertEquals(expectedUser, user);
    }

    @Test public void findByUsernameException() throws Exception {
        String username = "username";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        try {
            this.userService.findByUsername(username);
        } catch (Exception e) {
            if (e.getMessage().equals("Unknown user with username: " + username)) {
                assertTrue(true);
            } else {
                throw e;
            }
        }
    }

    @Test public void findByTokenOk() throws Exception {
        String token = "token";
        String userid = "userid";
        User expectedUser = new User();
        expectedUser.setId(userid);
        Mockito.when(userRepository.findByTokensId(token)).thenReturn(Optional.of(expectedUser));
        User user = this.userService.findByToken(token);
        assertEquals(expectedUser, user);
    }

    @Test public void findByTokenException() throws Exception {
        String token = "token";
        Mockito.when(userRepository.findByTokensId(token)).thenReturn(Optional.empty());
        try {
            this.userService.findByToken(token);
        } catch (Exception e) {
            if (e.getMessage().equals("Unknown user with token: " + token)) {
                assertTrue(true);
            } else {
                throw e;
            }
        }
    }

    @Test public void registerOk() throws Exception {
        String username = "username";
        String password = "password";
        String userid = "userid";
        User createdUser = new User();
        createdUser.setId(userid);
        createdUser.setUsername(username);
        RegisterInputDto registerInputDto = new RegisterInputDto(username, password);
        Mockito.when(roleService.findByValue(Mockito.anyString())).thenReturn(new Role());
        Mockito.when(scopeService.findByValue(Mockito.anyString())).thenReturn(new Scope());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(createdUser);
        RegisterOutputDto registerOutputDto = userService.register(registerInputDto);
        assertEquals(createdUser.getId(), registerOutputDto.getUserId());
        assertEquals(createdUser.getUsername(), registerOutputDto.getUsername());
    }

    @Test public void addTokenOk() {
        User user = new User();
        Token token = new Token();
        token.setId("token");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        userService.addToken(user, token);
        User expectedUser = new User();
        expectedUser.getTokens().add(token);
        assertEquals(expectedUser.getTokens(), user.getTokens());
    }

    @Test public void removeTokenOk() throws Exception {
        User user = new User();
        String tokenString = "token";
        Token token = new Token();
        token.setId(tokenString);
        user.getTokens().add(token);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userRepository.findByTokensId(tokenString)).thenReturn(Optional.of(user));
        userService.removeToken(tokenString);
        User expectedUser = new User();
        assertEquals(expectedUser.getTokens(), user.getTokens());
    }

    @Test public void findAllOk() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(authService.getUserInfoOutputDto(Mockito.anyString()))
            .thenReturn(new UserInfoOutputDto());
        List<UserInfoOutputDto> userInfoOutputDtos = userService.findAll();
        assertEquals(users.size(), userInfoOutputDtos.size());
    }

    @Test public void findAllException() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(authService.getUserInfoOutputDto(Mockito.any()))
            .thenThrow(new Exception("this.authService.getUserInfoOutputDto(user.getId()"));
        List<UserInfoOutputDto> userInfoOutputDtos = userService.findAll();
        assertEquals(0, userInfoOutputDtos.size());
    }


}
