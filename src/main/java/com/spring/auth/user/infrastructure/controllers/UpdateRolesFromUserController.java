package com.spring.auth.user.infrastructure.controllers;


import com.spring.auth.anotations.components.controllers.UserController;
import com.spring.auth.exceptions.application.DuplicatedKeyException;
import com.spring.auth.exceptions.application.NotFoundException;
import com.spring.auth.user.application.ports.UpdateUsersRolesPort;
import com.spring.auth.user.domain.User;
import com.spring.auth.user.infrastructure.dto.input.UpdateRolesFromUserInputDto;
import com.spring.auth.user.infrastructure.dto.output.UpdateRolesFromUserOutputDto;
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

/**
 * @author diegotobalina created on 24/06/2020
 */
@UserController
@AllArgsConstructor
@Validated
public class UpdateRolesFromUserController {

    private UpdateUsersRolesPort updateUsersRolesPort;

    @ApiOperation(value = "Update user roles", notes = "Actualiza los roles de un usuario")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "jwt",
                    dataType = "string",
                    paramType = "header",
                    required = true)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN') and hasPermission('hasAccess','UPDATE')")
    public UpdateRolesFromUserOutputDto update(
            @PathVariable @NotEmpty String userId, // todo: validate userId format
            @RequestBody @Valid UpdateRolesFromUserInputDto updateRolesFromUserInputDto)
            throws DuplicatedKeyException, NotFoundException {
        List<String> roleIds = updateRolesFromUserInputDto.getRole_ids();
        User updatedUser = updateUsersRolesPort.update(userId, roleIds);
        return new UpdateRolesFromUserOutputDto(updatedUser);
    }
}
