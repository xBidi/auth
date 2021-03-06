package com.spring.auth.user.application.ports;

import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.role.domain.Role;
import com.spring.auth.user.domain.User;

import java.util.List;

/** @author diegotobalina created on 24/06/2020 */
/** Add roles to a specific user */
public interface AddRolesToUserPort {
  User add(User user, List<Role> roles) throws DuplicatedKeyException;

  User add(String userId, List<String> roleIds) throws NotFoundException, DuplicatedKeyException;
}
