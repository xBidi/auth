package com.spring.auth.role.infrastructure.dtos.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/** @author diegotobalina created on 24/06/2020 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateScopesFromRoleInputDto {
  @NotEmpty private List<String> scope_ids; // todo: validate scopes format
}

