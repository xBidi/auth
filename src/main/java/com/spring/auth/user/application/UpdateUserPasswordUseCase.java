package com.spring.auth.user.application;

import com.spring.auth.anotations.components.UseCase;
import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.exceptions.application.WrongPasswordException;
import com.spring.auth.user.application.ports.in.UpdateUserPasswordPort;
import com.spring.auth.user.application.ports.out.FindUserByIdPort;
import com.spring.auth.user.application.ports.out.UpdateUserPort;
import com.spring.auth.user.domain.User;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class UpdateUserPasswordUseCase implements UpdateUserPasswordPort {

  private UpdateUserPort updateUserPort;
  private FindUserByIdPort findUserByIdPort;

  /**
   * @param user User which needs the password update
   * @param oldPassword Old password for the password check before the update
   * @param newPassword New password for the user
   * @return Updated user
   * @throws DuplicatedKeyException If there is some problem saving the user for duplications, like
   *     username
   * @throws WrongPasswordException If the old password is not correct
   */
  @Override
  public User update(User user, String oldPassword, String newPassword)
      throws DuplicatedKeyException, WrongPasswordException {
    if (!user.doPasswordsMatch(oldPassword))
      throw new WrongPasswordException("invalid old password");
    user.updatePassword(newPassword);
    return updateUserPort.update(user);
  }

  /**
   * Update the user password and save it in the database
   *
   * @param user User which needs the password update
   * @param password New password for the user
   * @return Updated user
   * @throws DuplicatedKeyException If there is some problem saving the user for duplications, like
   *     username
   */
  @Override
  public User update(User user, String password) throws DuplicatedKeyException {
    user.updatePassword(password);
    return updateUserPort.update(user);
  }

  /**
   * Update user password and save it in the databsae
   *
   * @param userId Find the user using the userId
   * @param oldPassword Old password for the password check before the update
   * @param newPassword New password for the user
   * @throws NotFoundException If the user was not found with the id
   * @throws DuplicatedKeyException If the user have some problems while saving it like duplicated
   *     username
   * @throws WrongPasswordException If the old password is not correct
   */
  @Override
  public void update(String userId, String oldPassword, String newPassword)
      throws NotFoundException, DuplicatedKeyException, WrongPasswordException {
    User user = findUserByIdPort.find(userId);
    update(user, oldPassword, newPassword);
  }

  /**
   * Update the user password and save it in the databsae
   *
   * @param userId Find the user using the userId
   * @param newPassword Set the new user password with this param
   * @throws NotFoundException If the user was not found
   * @throws DuplicatedKeyException If the user have some problems while saving it like duplicated
   *     username
   */
  @Override
  public void update(String userId, String newPassword)
      throws NotFoundException, DuplicatedKeyException {
    User user = findUserByIdPort.find(userId);
    update(user, newPassword);
  }
}