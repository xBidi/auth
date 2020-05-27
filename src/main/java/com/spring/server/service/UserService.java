package com.spring.server.service;

import com.example.demo.model.dto.*;
import com.spring.server.model.entity.ResetPasswordToken;
import com.spring.server.model.entity.SessionToken;
import com.spring.server.model.entity.User;
import com.spring.server.model.entity.VerifyEmailToken;
import com.spring.server.repository.UserRepository;
import com.spring.server.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User functions
 *
 * @author diegotobalina
 */
@Service @Slf4j public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired SessionTokenService sessionTokenService;
    @Autowired RoleService roleService;
    @Autowired ScopeService scopeService;
    @Autowired AuthService authService;
    @Autowired ResetPasswordTokenService resetPasswordTokenService;
    @Autowired VerifyEmailTokenService verifyEmailTokenService;


    public User findByUsernameOrEmail(String username, String email) {
        if (username != null && !username.isEmpty()) {
            return this.findByUsername(username);
        }
        if (email != null && !email.isEmpty()) {
            return this.findByEmail(email);
        }
        return null;
    }


    public RegisterOutputDto register(RegisterInputDto registerInputDto) throws Exception {
        String username = registerInputDto.getUsername();
        String email = registerInputDto.getEmail();
        String password = registerInputDto.getPassword();
        User user =
            new User(username, email, password, false, new ArrayList<>(), new ArrayList<>());
        User createdUser = createUser(user);
        RegisterOutputDto registerOutputDto =
            new RegisterOutputDto(createdUser.getId(), createdUser.getUsername(), email);
        return registerOutputDto;
    }

    public List<String> checkDatabaseConstraints(User user) {
        List<String> errors = new ArrayList<>();
        String username = user.getUsername();
        String email = user.getEmail();
        if (this.userRepository.findByUsername(username).isPresent()) {
            errors.add("There is already an account with username: " + username);
        }
        if (this.userRepository.findByEmail(email).isPresent()) {
            errors.add("There is already an account with email: " + email);
        }
        return errors;
    }

    @Transactional(rollbackFor = Exception.class) public User createUser(User user)
        throws Exception {
        user.setId(null);
        setDefaultRoles(user);
        setDefaultScopes(user);
        List<String> errors = checkDatabaseConstraints(user);
        if (!errors.isEmpty()) {
            throw new Exception(errors.toString());
        }
        user = userRepository.save(user);
        VerifyEmailToken verifyEmailToken = verifyEmailTokenService.generateToken();
        addVerifyEmailToken(user, verifyEmailToken);
        mailService.mailNewUser(user.getEmail(), verifyEmailToken.getToken());
        return user;
    }

    private void setDefaultRoles(User user) {
        user.getRoles().add(roleService.findByValue("ROLE_USER"));
    }

    private void setDefaultScopes(User user) {
        user.getScopes().add(scopeService.findByValue("READ_USER"));
        user.getScopes().add(scopeService.findByValue("CREATE_USER"));
        user.getScopes().add(scopeService.findByValue("MODIFY_USER"));
        user.getScopes().add(scopeService.findByValue("DELETE_USER"));
    }


    public void addSessionToken(User user, SessionToken sessionToken) {
        user.getSessionTokens().add(sessionToken);
        this.userRepository.save(user);
    }


    public void addResetPasswordToken(User user, ResetPasswordToken resetPasswordToken) {
        user.getResetPasswordTokens().add(resetPasswordToken);
        this.userRepository.save(user);
    }


    public void addVerifyEmailToken(User user, VerifyEmailToken verifyEmailToken) {
        user.getVerifyEmailTokens().add(verifyEmailToken);
        this.userRepository.save(user);
    }


    public void removeSessionToken(String tokenString) {
        User user = this.findBySessionTokensToken(tokenString);
        user.getSessionTokens().removeIf(token -> token.getToken().equals(tokenString));
        this.userRepository.save(user);
    }

    public List<UserInfoOutputDto> findAll() {
        List<User> users = this.userRepository.findAll();
        List<UserInfoOutputDto> userInfoOutputDtos = new ArrayList<>();
        for (User user : users) {
            userInfoOutputDtos.add(userToUserInfoOutputDto(user));
        }
        return userInfoOutputDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserInfoOutputDto userToUserInfoOutputDto(User user) {
        List<String> roles =
            user.getRoles().stream().map(role -> role.getValue()).collect(Collectors.toList());
        List<String> scopes =
            user.getScopes().stream().map(scope -> scope.getValue()).collect(Collectors.toList());
        for (SessionToken sessionToken : user.getSessionTokens()) {
            if (!sessionTokenService.isValid(sessionToken)) {
                sessionTokenService.removeToken(sessionToken);
            }
        }
        List<TokenDto> sessions = user.getSessionTokens().stream().map(
            sessionToken -> new TokenDto(sessionToken.getToken(),
                sessionToken.getExpeditionDate().toString(),
                sessionToken.getExpirationDate().toString())).collect(Collectors.toList());
        return new UserInfoOutputDto(user.getId(), user.getUsername(), user.getEmail(),
            user.getEmailVerified(), roles, scopes, sessions);
    }

    public User findById(String id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    public User findByEmail(String email) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    public User findByUsername(String username) {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    public User findBySessionTokensToken(String token) {
        Optional<User> optionalUser = this.userRepository.findBySessionTokensToken(token);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    public User findByVerifyEmailTokensToken(String token) {
        Optional<User> optionalUser = this.userRepository.findByVerifyEmailTokensToken(token);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    public User findByResetPasswordTokensToken(String token) {
        Optional<User> optionalUser = this.userRepository.findByResetPasswordTokensToken(token);
        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }


    public void removeResetPasswordToken(String tokenString) {
        User user = this.findByResetPasswordTokensToken(tokenString);
        user.getResetPasswordTokens().removeIf(token -> token.getToken().equals(tokenString));
        this.userRepository.save(user);
    }


    public void updatePassword(Principal principal, UpdateUserPasswordDto updateUserPasswordDto)
        throws Exception {
        String loggedUserId = authService.findByPrincipal(principal).getUserId();
        User user = findById(loggedUserId);
        String newPassword = updateUserPasswordDto.getNewPassword();
        String oldPassword = updateUserPasswordDto.getOldPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }

    @Autowired private MailService mailService;

    public void sendResetPasswordEmail(SendResetPasswordEmailDto sendResetPasswordEmailDto)
        throws IOException, MessagingException {
        String email = sendResetPasswordEmailDto.getEmail();
        User user = findByEmail(email);
        if (user == null) {
            return; // invalid user
        }
        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.generateToken();
        addResetPasswordToken(user, resetPasswordToken);
        mailService.mailResetPassword(email, resetPasswordToken.getToken());
    }


    @Transactional(rollbackFor = Exception.class)
    public void resetPasswordWithEmail(ResetPasswordWithEmailDto resetPasswordWithEmailDto) {
        String token = resetPasswordWithEmailDto.getToken();
        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.findByToken(token);
        if (resetPasswordToken == null) {
            return;
        }
        if (!resetPasswordTokenService.isValid(resetPasswordToken)) {
            resetPasswordTokenService.removeToken(resetPasswordToken);
            return;
        }
        User user = findByResetPasswordTokensToken(token);
        user.setPassword(resetPasswordWithEmailDto.getNewPassword());
        userRepository.save(user);
        resetPasswordTokenService.removeToken(resetPasswordToken);
    }


    public void removeVerifyEmailToken(String tokenString) {
        User user = this.findByVerifyEmailTokensToken(tokenString);
        user.getVerifyEmailTokens().removeIf(token -> token.getToken().equals(tokenString));
        this.userRepository.save(user);
    }

    public void sendVerifyEmailEmail(Principal principal) throws Exception {
        String userId = authService.findByPrincipal(principal).getUserId();
        User user = findById(userId);
        if (user == null || user.getEmailVerified()) {
            return; // invalid user
        }
        String email = user.getEmail();
        VerifyEmailToken verifyEmailToken = verifyEmailTokenService.generateToken();
        addVerifyEmailToken(user, verifyEmailToken);
        mailService.mailVerifyEmail(email, verifyEmailToken.getToken());
    }

    @Transactional(rollbackFor = Exception.class)
    public void verifyEmail(VerifyEmailDto verifyEmailDto) {
        String token = verifyEmailDto.getToken();
        VerifyEmailToken verifyEmailToken = verifyEmailTokenService.findByToken(token);
        if (verifyEmailToken == null) {
            return;
        }
        if (!verifyEmailTokenService.isValid(verifyEmailToken)) {
            verifyEmailTokenService.removeToken(verifyEmailToken);
            return;
        }
        User user = findByVerifyEmailTokensToken(token);
        user.setEmailVerified(true);
        userRepository.save(user);
        verifyEmailTokenService.removeToken(verifyEmailToken);
    }
}
