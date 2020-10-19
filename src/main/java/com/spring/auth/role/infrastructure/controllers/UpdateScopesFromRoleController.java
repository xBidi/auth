package com.spring.auth.role.infrastructure.controllers;

import com.spring.auth.anotations.components.controllers.RoleController;
import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.role.application.ports.UpdateScopesFromRolePort;
import com.spring.auth.role.domain.Role;
import com.spring.auth.role.infrastructure.dtos.input.UpdateScopesFromRoleInputDto;
import com.spring.auth.role.infrastructure.dtos.output.UpdateScopesFromRoleOutputDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/** @author diegotobalina created on 24/06/2020 */
@RoleController
@AllArgsConstructor
@Validated
public class UpdateScopesFromRoleController {

  private UpdateScopesFromRolePort updateScopesFromRolePort;

  @ApiOperation(value = "Update scopes", notes = "Actualiza las scopes de un role")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "Authorization",
        value = "jwt",
        dataType = "string",
        paramType = "header",
        required = true)
  })
  @ResponseStatus(HttpStatus.CREATED)
  @PutMapping("/{roleId}/scopes")
  @PreAuthorize("hasRole('ADMIN') and hasPermission('hasAccess','UPDATE')")
  public UpdateScopesFromRoleOutputDto update(
          @PathVariable @NotEmpty String roleId, // todo: validate roleId format
          @RequestBody @Valid UpdateScopesFromRoleInputDto updateScopesFromRoleInputDto)
      throws NotFoundException, DuplicatedKeyException {
    List<String> scopes = updateScopesFromRoleInputDto.getScope_ids();
    Role roleUpdated = updateScopesFromRolePort.update(roleId, scopes);
    return new UpdateScopesFromRoleOutputDto(roleUpdated);
  }
}
