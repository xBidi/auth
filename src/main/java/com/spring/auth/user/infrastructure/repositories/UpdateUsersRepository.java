package com.spring.auth.user.infrastructure.repositories;

import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.user.application.ports.out.UpdateUsersPort;
import com.spring.auth.user.domain.User;
import com.spring.auth.user.domain.UserJpa;
import com.spring.auth.user.domain.UserMapper;
import com.spring.auth.user.infrastructure.repositories.jpa.UserRepositoryJpa;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class UpdateUsersRepository implements UpdateUsersPort {

  private UserRepositoryJpa userRepositoryJpa;

  /**
   * Save all the existing users in the database with the new data. Before save the users check for
   * duplicated usernames o duplicated emails.
   *
   * @param users Users that must be updated
   * @return List with the updated users
   * @throws DuplicatedKeyException When some constraint fails, for example duplicated username
   */
  @Override
  public List<User> updateAll(List<User> users) throws DuplicatedKeyException {
    List<String> ids = users.stream().map(User::getId).collect(Collectors.toList());

    // username must be unique in the database
    List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());
    if (userRepositoryJpa.existsByUsernameInAndIdNotIn(
        usernames, ids)) // todo: duplicated comprobation
    throw new DuplicatedKeyException("duplicated username in: " + usernames);

    // email must be unique in the database
    List<String> emails = users.stream().map(User::getEmail).collect(Collectors.toList());
    if (userRepositoryJpa.existsByEmailInAndIdNotIn(emails, ids)) // todo: duplicated comprobation
    throw new DuplicatedKeyException("duplicated email in: " + emails);

    List<UserJpa> usersJpa = users.stream().map(UserMapper::parse).collect(Collectors.toList());
    List<UserJpa> savedUsersJpa = userRepositoryJpa.saveAll(usersJpa);
    return savedUsersJpa.stream().map(UserMapper::parse).collect(Collectors.toList());
  }
}