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

/**
 * User functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired TokenService tokenService;
    @Autowired RoleService roleService;
    @Autowired ScopeService scopeService;
    @Autowired AuthService authService;

    public User findById(String id) throws Exception {
        log.debug("{findById start}");
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            log.debug("{findById end} Unknown user with id: " + id);
            throw new Exception("Unknown user with id: " + id);
        }
        log.debug("{findById end}");
        return optionalUser.get();
    }


    public User findByUsername(String username) throws Exception {
        log.debug("{findByUsername start}");
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            log.debug("{findByUsername end} Unknown user with username: " + username);
            throw new Exception("Unknown user with username: " + username);
        }
        log.debug("{findByUsername end}");
        return optionalUser.get();
    }

    public User findByToken(String token) throws Exception {
        log.debug("{findByToken start}");
        Optional<User> optionalUser = this.userRepository.findByTokensToken(token);
        if (!optionalUser.isPresent()) {
            log.debug("{findByToken end} Unknown user with token: " + token);
            throw new Exception("Unknown user with token: " + token);
        }
        log.debug("{findByToken end}");
        return optionalUser.get();
    }

    public RegisterOutputDto register(RegisterInputDto registerInputDto) throws Exception {
        log.debug("{register start}");
        String username = registerInputDto.getUsername();
        // test username duplicated
        if (this.userRepository.findByUsername(username).isPresent()) {
            log.debug("{register end} There is already an account with username: " + username);
            throw new Exception("There is already an account with username: " + username);
        }
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
        User createdUser = this.userRepository.save(user);
        RegisterOutputDto registerOutputDto =
            new RegisterOutputDto(createdUser.getId(), createdUser.getUsername());
        log.debug("{register end}");
        return registerOutputDto;
    }

    public User addToken(User user, Token token) {
        log.debug("{register start}");
        user.getTokens().add(token);
        this.userRepository.save(user);
        log.debug("{register end}");
        return user;
    }

    public void removeToken(String tokenString) throws Exception {
        log.debug("{removeToken start}");
        User user = this.findByToken(tokenString);
        user.getTokens().removeIf(token -> token.getToken().equals(tokenString));
        this.userRepository.save(user);
        log.debug("{removeToken end}");
    }

    public List<UserInfoOutputDto> findAll() {
        log.debug("{findAll start}");
        List<User> users = this.userRepository.findAll();
        List<UserInfoOutputDto> userInfoOutputDtos = new ArrayList<>();
        users.forEach(user -> {
            try {
                userInfoOutputDtos.add(this.authService.getUserInfoOutputDto(user.getId()));
            } catch (Exception ex) {
                log.warn("{findAll exception} (ex)" + ex.getMessage());
            }
        });
        log.debug("{findAll end}");
        return userInfoOutputDtos;
    }

}
