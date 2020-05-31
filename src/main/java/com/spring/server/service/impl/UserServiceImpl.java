package com.spring.server.service.impl;

import com.spring.server.model.dto.*;
import com.spring.server.model.entity.ResetPasswordToken;
import com.spring.server.model.entity.SessionToken;
import com.spring.server.model.entity.User;
import com.spring.server.model.entity.VerifyEmailToken;
import com.spring.server.repository.UserRepository;
import com.spring.server.service.*;
import com.spring.server.service.interfaces.UserService;
import com.spring.server.util.PasswordUtil;
import com.spring.server.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User functions
 *
 * @author diegotobalina
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private SessionTokenService sessionTokenService;
  @Autowired private RoleService roleService;
  @Autowired private ScopeService scopeService;
  @Autowired private AuthServiceImpl authServiceImpl;
  @Autowired private ResetPasswordTokenService resetPasswordTokenService;
  @Autowired private VerifyEmailTokenService verifyEmailTokenService;
  @Autowired private MailService mailServiceImpl;

  @Override
  public RegisterOutputDto register(RegisterInputDto registerInputDto) throws Exception {
    final String username = registerInputDto.getUsername();
    final String email = registerInputDto.getEmail();
    final String password = registerInputDto.getPassword();
    final User user =
        new User(username, email, password, false, new ArrayList<>(), new ArrayList<>());
    final User createdUser = createUser(user);
    return new RegisterOutputDto(createdUser);
  }

  @Override
  public List<UserInfoOutputDto> findAll() {
    final List<User> users = this.userRepository.findAll();
    final List<UserInfoOutputDto> userInfoOutputDtos = new ArrayList<>();
    for (final User user : users) {
      userInfoOutputDtos.add(new UserInfoOutputDto(user));
    }
    return userInfoOutputDtos;
  }

  @Override
  public void sendVerifyEmailEmail(Principal principal) throws Exception {
    final String userId = UserUtil.getUserIdFromPrincipal(principal);
    final User user = findById(userId);
    if (user == null || user.getEmailVerified()) {
      return; // invalid user
    }
    final String email = user.getEmail();
    final VerifyEmailToken verifyEmailToken = verifyEmailTokenService.generateToken();
    this.addVerifyEmailToken(user, verifyEmailToken);
    mailServiceImpl.mailVerifyEmail(email, verifyEmailToken.getToken());
  }

  @Override
  public void updatePassword(Principal principal, UpdateUserPasswordDto updateUserPasswordDto) {
    final String userId = UserUtil.getUserIdFromPrincipal(principal);
    final User user = findById(userId);
    final String newPassword = updateUserPasswordDto.getNewPassword();
    final String oldPassword = updateUserPasswordDto.getOldPassword();
    if (PasswordUtil.doPasswordsMatch(newPassword, oldPassword)) {
      user.setPassword(newPassword);
      this.userRepository.save(user);
    }
  }

  @Override
  public void sendResetPasswordEmail(SendResetPasswordEmailDto sendResetPasswordEmailDto)
      throws IOException, MessagingException {
    final String email = sendResetPasswordEmailDto.getEmail();
    final User user = this.findByEmail(email);
    if (user == null) {
      return; // invalid user
    }
    final ResetPasswordToken resetPasswordToken = resetPasswordTokenService.generateToken();
    this.addResetPasswordToken(user, resetPasswordToken);
    this.mailServiceImpl.mailResetPassword(email, resetPasswordToken.getToken());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void resetPasswordWithEmail(ResetPasswordWithEmailDto resetPasswordWithEmailDto) {
    final String token = resetPasswordWithEmailDto.getToken();
    final ResetPasswordToken resetPasswordToken = resetPasswordTokenService.findByToken(token);
    if (resetPasswordToken == null) {
      return;
    }
    if (!(this.resetPasswordTokenService.isValid(resetPasswordToken))) {
      this.resetPasswordTokenService.removeToken(resetPasswordToken);
      return;
    }
    final User user = findByResetPasswordTokensToken(token);
    user.setPassword(resetPasswordWithEmailDto.getNewPassword());
    this.userRepository.save(user);
    this.resetPasswordTokenService.removeToken(resetPasswordToken);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void verifyEmail(VerifyEmailDto verifyEmailDto) {
    final String token = verifyEmailDto.getToken();
    final VerifyEmailToken verifyEmailToken = verifyEmailTokenService.findByToken(token);
    if (verifyEmailToken == null) {
      return;
    }
    if (!(this.verifyEmailTokenService.isValid(verifyEmailToken))) {
      this.verifyEmailTokenService.removeToken(verifyEmailToken);
      return;
    }
    final User user = findByVerifyEmailTokensToken(token);
    user.setEmailVerified(true);
    this.userRepository.save(user);
    this.verifyEmailTokenService.removeToken(verifyEmailToken);
  }

  public User findByUsernameOrEmail(String username, String email) {
    if (username != null && !username.isEmpty()) {
      return this.findByUsername(username);
    }
    if (email != null && !email.isEmpty()) {
      return this.findByEmail(email);
    }
    return null;
  }

  @Transactional(rollbackFor = Exception.class)
  public User createUser(User user) throws Exception {
    user.setId(null);
    this.setDefaultRoles(user);
    this.setDefaultScopes(user);
    final List<String> errors = checkDatabaseConstraints(user);
    if (!errors.isEmpty()) {
      throw new Exception(errors.toString());
    }
    final User createdUser = userRepository.save(user);
    final VerifyEmailToken verifyEmailToken = verifyEmailTokenService.generateToken();
    this.addVerifyEmailToken(createdUser, verifyEmailToken);
    this.mailServiceImpl.mailNewUser(createdUser.getEmail(), verifyEmailToken.getToken());
    return createdUser;
  }

  private List<String> checkDatabaseConstraints(User user) {
    final List<String> errors = new ArrayList<>();
    final String username = user.getUsername();
    final String email = user.getEmail();
    if (this.userRepository.findByUsername(username).isPresent()) {
      errors.add("There is already an account with username: " + username);
    }
    if (this.userRepository.findByEmail(email).isPresent()) {
      errors.add("There is already an account with email: " + email);
    }
    return errors;
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

  public User findById(String id) {
    final Optional<User> optionalUser = this.userRepository.findById(id);
    if (!optionalUser.isPresent()) {
      return null;
    }
    return optionalUser.get();
  }

  public User findByEmail(String email) {
    final Optional<User> optionalUser = this.userRepository.findByEmail(email);
    if (!optionalUser.isPresent()) {
      return null;
    }
    return optionalUser.get();
  }

  public User findByUsername(String username) {
    final Optional<User> optionalUser = this.userRepository.findByUsername(username);
    if (!optionalUser.isPresent()) {
      return null;
    }
    return optionalUser.get();
  }

  public User findBySessionTokensToken(String token) {
    final Optional<User> optionalUser = this.userRepository.findBySessionTokensToken(token);
    if (!optionalUser.isPresent()) {
      return null;
    }
    return optionalUser.get();
  }

  public User findByVerifyEmailTokensToken(String token) {
    final Optional<User> optionalUser = this.userRepository.findByVerifyEmailTokensToken(token);
    if (!optionalUser.isPresent()) {
      return null;
    }
    return optionalUser.get();
  }

  public User findByResetPasswordTokensToken(String token) {
    final Optional<User> optionalUser = this.userRepository.findByResetPasswordTokensToken(token);
    if (!optionalUser.isPresent()) {
      return null;
    }
    return optionalUser.get();
  }

  public void removeResetPasswordToken(String tokenString) {
    final User user = this.findByResetPasswordTokensToken(tokenString);
    user.getResetPasswordTokens().removeIf(token -> token.getToken().equals(tokenString));
    this.userRepository.save(user);
  }

  public void removeVerifyEmailToken(String tokenString) {
    final User user = this.findByVerifyEmailTokensToken(tokenString);
    user.getVerifyEmailTokens().removeIf(token -> token.getToken().equals(tokenString));
    this.userRepository.save(user);
  }
}
