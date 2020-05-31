package com.spring.server.controller;

import com.spring.server.model.dto.*;
import com.spring.server.model.validator.JwtTokenConstraint;
import com.spring.server.service.interfaces.UserService;
import io.swagger.annotations.Api;
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
@RestController
@RequestMapping("api/v1/users")
@Api(tags = "users")
@ResponseStatus(HttpStatus.OK)
@Slf4j
@Validated
public class UserController {

  @Autowired private UserService userService;

  @ApiOperation(value = "Register", notes = "Registra un nuevo usuario en la base de datos")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("register")
  public RegisterOutputDto register(@RequestBody @Valid RegisterInputDto registerInputDto)
      throws Exception {
    return this.userService.register(registerInputDto);
  }

  @ApiOperation(
      value = "Update password",
      notes = "Actualiza la contraseña del usuario relacionado con el token de acceso")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "Authorization",
        value = "jwt",
        dataType = "string",
        paramType = "header",
        required = true)
  })
  @PreAuthorize("hasRole('USER') and hasPermission('hasAccess','MODIFY_USER')")
  @PutMapping("password")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePassword(
      @RequestHeader("Authorization") @JwtTokenConstraint final String authorization,
      final Principal principal,
      @RequestBody @Valid final UpdateUserPasswordDto updateUserPasswordDto)
      throws Exception {
    this.userService.updatePassword(principal, updateUserPasswordDto);
  }

  @ApiOperation(
      value = "Reset password with email token",
      notes =
          "Permite cambiar la contraseña de un usuario mediante el token que ha recibido por correo")
  @PutMapping("password/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetPasswordWithEmail(
      @RequestBody @Valid final ResetPasswordWithEmailDto resetPasswordWithEmailDto) {
    this.userService.resetPasswordWithEmail(resetPasswordWithEmailDto);
  }

  @ApiOperation(
      value = "Send reset password token to mail",
      notes = "Envía un correo con un token para recuperar la contraseña")
  @PostMapping("password/email")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void sendResetPasswordEmail(
      @RequestBody @Valid final SendResetPasswordEmailDto sendResetPasswordEmailDto)
      throws Exception {
    this.userService.sendResetPasswordEmail(sendResetPasswordEmailDto);
  }

  @ApiOperation(
      value = "Send validate email token to email",
      notes = "Envía un correo con un token para validar el email")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "Authorization",
        value = "jwt",
        dataType = "string",
        paramType = "header",
        required = true)
  })
  @PreAuthorize("hasRole('USER') and hasPermission('hasAccess','MODIFY_USER')")
  @PostMapping("email")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void sendVerifyEmailEmail(
      @RequestHeader("Authorization") @JwtTokenConstraint final String authorization,
      final Principal principal)
      throws Exception {
    this.userService.sendVerifyEmailEmail(principal);
  }

  @ApiOperation(
      value = "Verify email with email token",
      notes = "Verifica el correo electrónico mediante el token que ha recibido por correo")
  @PostMapping("email/verify")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void verifyEmailEmail(@Valid @RequestBody final VerifyEmailDto verifyEmailDto) {
    this.userService.verifyEmail(verifyEmailDto);
  }

  @ApiOperation(value = "Get all users", notes = "Devuelve todos los usuarios en la base de datos")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "Authorization",
        value = "jwt",
        dataType = "string",
        paramType = "header",
        required = true)
  })
  @GetMapping
  @PreAuthorize("hasRole('ADMIN') and hasPermission('hasAccess','READ')")
  public List<UserInfoOutputDto> users(
      @RequestHeader("Authorization") @JwtTokenConstraint final String authorization) {
    return userService.findAll();
  }
}
