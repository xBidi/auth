package com.example.demo.service;

import com.example.demo.model.dto.RegisterInputDto;
import com.example.demo.model.dto.RegisterOutputDto;
import com.example.demo.model.dto.UserInfoOutputDto;
import com.example.demo.model.entity.Role;
import com.example.demo.model.entity.Scope;
import com.example.demo.model.entity.Token;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service @Slf4j public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired TokenService tokenService;
    @Autowired RoleService roleService;
    @Autowired ScopeService scopeService;
    @Autowired AuthService authService;

    public User findById(String id) throws Exception {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new Exception("Unknown user with id: " + id);
        }
        return optionalUser.get();
    }


    public User findByUsername(String username) throws Exception {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new Exception("Unknown user with username: " + username);
        }
        return optionalUser.get();
    }

    public User findByToken(String token) throws Exception {
        Optional<User> optionalUser = this.userRepository.findByTokensId(token);
        if (!optionalUser.isPresent()) {
            throw new Exception("Unknown user with token: " + token);
        }
        return optionalUser.get();
    }

    public RegisterOutputDto register(RegisterInputDto registerInputDto) throws Exception {
        String username = registerInputDto.getUsername();
        String password = registerInputDto.getPassword();
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findByValue("ROLE_USER"));
        roles.add(roleService.findByValue("ROLE_ADMIN"));
        List<Scope> scopes = new ArrayList<>();
        scopes.add(scopeService.findByValue("READ"));
        scopes.add(scopeService.findByValue("CREATE"));
        scopes.add(scopeService.findByValue("MODIFY"));
        scopes.add(scopeService.findByValue("DELETE"));
        User user = new User(username, password, roles, scopes);
        // test username duplicated
        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new Exception("There is already an account with username: " + username);
        }
        User createdUser = this.userRepository.save(user);
        return new RegisterOutputDto(createdUser.getId(), createdUser.getUsername());
    }

    public User addToken(User user, Token token) {
        user.getTokens().add(token);
        return this.userRepository.save(user);
    }

    public void removeToken(String tokenString) throws Exception {
        User user = this.findByToken(tokenString);
        user.getTokens().removeIf(token -> token.getId().equals(tokenString));
        this.userRepository.save(user);
    }

    public List<UserInfoOutputDto> findAll() {
        List<User> users = this.userRepository.findAll();
        List<UserInfoOutputDto> userInfoOutputDtos = new ArrayList<>();
        users.forEach(user -> {
            try {
                userInfoOutputDtos.add(this.authService.getUserInfoOutputDto(user.getId()));
            } catch (Exception ex) {
                log.warn(ex.getMessage());
            }
        });
        return userInfoOutputDtos;
    }

}
