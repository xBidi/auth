package com.spring.auth.role.infrastructure.dtos.output;

import com.spring.auth.role.domain.Role;
import com.spring.auth.scope.domain.Scope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author diegotobalina created on 24/06/2020
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateScopesFromRoleOutputDto {

    private String id;
    private String name;
    private String description;
    private String value;
    private List<String> scopes = new ArrayList<>();

    public UpdateScopesFromRoleOutputDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.description = role.getDescription();
        this.value = role.getValue();
        for (Scope scope : role.getScopes()) {
            this.scopes.add(scope.getId());
        }
    }
}
