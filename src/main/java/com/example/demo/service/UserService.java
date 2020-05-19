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

    public User findByEmail(String email) throws Exception {
        log.debug("{findByEmail start}");
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            log.debug("{findById end} Unknown user with email: " + email);
            throw new Exception("Unknown user with email: " + email);
        }
        log.debug("{findByEmail end}");
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
        String email = registerInputDto.getEmail();
        if (this.userRepository.findByEmail(email).isPresent()) {
            log.debug("{register end} There is already an account with email: " + email);
            throw new Exception("There is already an account with email: " + email);
        }
        String password = registerInputDto.getPassword();
        User user = new User(username, email, password, new ArrayList<>(), new ArrayList<>());
        User createdUser = createIfNotExist(user);
        RegisterOutputDto registerOutputDto =
            new RegisterOutputDto(createdUser.getId(),  createdUser.getUsername(),email);
        log.debug("{register end}");
        return registerOutputDto;
    }

    public User createIfNotExist(User user) throws Exception {
        log.debug("{createIfNotExist start}");
        String username = user.getUsername();
        // test username duplicated
        Optional<User> optionalUser;
        if ((optionalUser = this.userRepository.findByUsername(username)).isPresent()) {
            log.debug(
                "{createIfNotExist end} There is already an account with username: " + username);
            return optionalUser.get();
        }
        String email = user.getEmail();
        if ((optionalUser = this.userRepository.findByEmail(email)).isPresent()) {
            log.debug("{createIfNotExist end} There is already an account with email: " + email);
            return optionalUser.get();
        }
        user.getRoles().add(roleService.findByValue("ROLE_USER"));
        user.getScopes().add(scopeService.findByValue("READ"));
        user.getScopes().add(scopeService.findByValue("CREATE"));
        user.getScopes().add(scopeService.findByValue("MODIFY"));
        user.getScopes().add(scopeService.findByValue("DELETE"));
        User savedUser = this.userRepository.save(user);
        log.debug("{createIfNotExistByEmail end} saved user: " + savedUser.toString());
        return savedUser;
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
