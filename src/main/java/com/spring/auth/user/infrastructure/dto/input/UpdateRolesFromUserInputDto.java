package com.spring.auth.user.infrastructure.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/** @author diegotobalina created on 24/06/2020 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateRolesFromUserInputDto {
  // id of the roles
  @NotEmpty private List<String> role_ids = new ArrayList<>(); // todo: validate roleIds format
}
