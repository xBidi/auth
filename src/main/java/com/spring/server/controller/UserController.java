package com.spring.server.controller;

import com.spring.server.model.dto.*;
import com.spring.server.model.validator.JwtTokenConstraint;
import com.spring.server.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * User endpoints
 *
 * @author diegotobalina
 */
@RestController @RequestMapping("api/v1/users")
@ResponseStatus(HttpStatus.OK) @Slf4j @Validated public class UserController {

    @Autowired UserService userService;

    @ApiOperation(value = "Register a new user in the database", notes = "")
    @ResponseStatus(HttpStatus.CREATED) @PostMapping("register")
    public RegisterOutputDto register(@RequestBody @Valid RegisterInputDto registerInputDto)
        throws Exception {
        return this.userService.register(registerInputDto);
    }

    @ApiOperation(value = "Update user password", notes = "") @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @PreAuthorize("hasRole('USER') and hasPermission('hasAccess','MODIFY_USER')")
    @PutMapping("password") @ResponseStatus(HttpStatus.NO_CONTENT) public void updatePassword(
        @RequestHeader("Authorization") @JwtTokenConstraint String authorization,
        Principal principal, @RequestBody @Valid UpdateUserPasswordDto updateUserPasswordDto)
        throws Exception {
        this.userService.updatePassword(principal, updateUserPasswordDto);
    }

    @ApiOperation(value = "recupera la contraseña usando el token recibido por correo", notes = "")
    @PutMapping("password/reset") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPasswordWithEmail(
        @RequestBody @Valid ResetPasswordWithEmailDto resetPasswordWithEmailDto) {
        this.userService.resetPasswordWithEmail(resetPasswordWithEmailDto);
    }

    @ApiOperation(value = "envia un correo para recuperar la contraseña", notes = "")
    @PostMapping("password/email") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendResetPasswordEmail(
        @RequestBody @Valid SendResetPasswordEmailDto sendResetPasswordEmailDto) throws Exception {
        this.userService.sendResetPasswordEmail(sendResetPasswordEmailDto);
    }

    @ApiOperation(value = "envia un correo para verificar el email", notes = "")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @PostMapping("email") @ResponseStatus(HttpStatus.NO_CONTENT) public void sendVerifyEmailEmail(
        @RequestHeader("Authorization") @JwtTokenConstraint String authorization,
        Principal principal) throws Exception {
        this.userService.sendVerifyEmailEmail(principal);
    }

    @ApiOperation(value = "verifica el correo electronico", notes = "") @PostMapping("email/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyEmailEmail(@Valid @RequestBody VerifyEmailDto verifyEmailDto) {
        this.userService.verifyEmail(verifyEmailDto);
    }

    @ApiOperation(value = "list all database users", notes = "ONLY ADMINS WITH READ+ SCOPE")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @GetMapping @PreAuthorize("hasRole('ADMIN') and hasPermission('hasAccess','READ')")
    public List<UserInfoOutputDto> users(
        @RequestHeader("Authorization") @JwtTokenConstraint String authorization) {
        return userService.findAll();
    }



}
