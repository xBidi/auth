package com.spring.auth.user.infrastructure.dto.output;


import com.spring.auth.role.domain.Role;
import com.spring.auth.scope.domain.Scope;
import com.spring.auth.user.domain.User;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author diegotobalina created on 24/06/2020
 */
@Getter
@ToString
public class UpdateRolesFromUserOutputDto {

    private String id; // id of the user
    private String username; // username of the user
    private String email; // email of the user
    private List<String> roles; // role ids of the user
    private List<String> scopes; // scope ids of the user

    public UpdateRolesFromUserOutputDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        this.scopes = user.getScopes().stream().map(Scope::getId).collect(Collectors.toList());
    }
}
