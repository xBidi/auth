package com.spring.auth.role.application.ports;


import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.role.domain.Role;

import java.util.List;

/** @author diegotobalina created on 24/06/2020 */
public interface UpdateScopesFromRolePort {
  Role update(Role role, List<String> scopeIds) throws DuplicatedKeyException;
  Role update(String roleId, List<String> scopeIds) throws NotFoundException, DuplicatedKeyException, NotFoundException;
}
