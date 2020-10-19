package com.spring.auth.user.application.ports;

import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.role.domain.Role;
import com.spring.auth.user.domain.User;

import java.util.List;

/**
 * @author diegotobalina created on 24/06/2020
 */
public interface UpdateUsersRolesPort {

    List<User> updateUsersRoles(List<Role> roles) throws DuplicatedKeyException;

    User update(User user, List<Role> roles) throws DuplicatedKeyException;

    User update(String userId, List<String> roleIds) throws DuplicatedKeyException, NotFoundException;
}
