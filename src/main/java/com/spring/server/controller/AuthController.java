package com.spring.server.controller;

import com.spring.server.model.dto.*;
import com.spring.server.model.validator.JwtTokenConstraint;
import com.spring.server.model.validator.TokenConstraint;
import com.spring.server.service.interfaces.AuthService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

/**
 * Authorization endpoints
 *
 * @author diegotobalina
 */
@RestController @RequestMapping("api/v1/oauth2") @ResponseStatus(HttpStatus.OK)
@Api(tags = "oauth2") @Slf4j @Validated public class AuthController {

    @Autowired private AuthService authService;

    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Invalid credentials")})
    @ApiOperation(value = "Login", notes = "Inicia sesión con el nombre de usuario o correo y la contraseña ")
    @ResponseStatus(HttpStatus.CREATED) @PostMapping("login")
    public LoginOutputDto login(@RequestBody @Valid final LoginInputDto loginInputDto)
        throws Exception {
        return this.authService.login(loginInputDto);
    }

    @ApiOperation(value = "Logout", notes = "Cierra una sesión activa mendiante su token")
    @ResponseStatus(HttpStatus.NO_CONTENT) @DeleteMapping("logout")
    public void logout(@RequestBody @Valid final LogoutInputDto logoutInputDto) {
        this.authService.logout(logoutInputDto);
    }

    @ApiOperation(value = "Access", notes = "Obtiene un token de acceso mediante el token recibido en la llamada login")
    @ResponseStatus(HttpStatus.OK) @PostMapping("access")
    public AccessOutputDto access(@RequestBody @Valid final AccessInputDto accessInputDto)
        throws Exception {
        return this.authService.access(accessInputDto);
    }

    @ApiOperation(value = "Token info", notes = "Devuelve la información relacionada con un token, puede ser de sesión, de acceso o de google")
    @ResponseStatus(HttpStatus.OK) @GetMapping("tokenInfo") public TokenInfoOutputDto tokenInfo(
        @RequestParam(name = "token") @TokenConstraint final String token) throws Exception {
        return this.authService.tokenInfo(token);
    }

    @ApiOperation(value = "User info", notes = "Devuelve la información de un usuario relacionado con un token, puede ser de acceso o de google")
    @ResponseStatus(HttpStatus.OK) @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @GetMapping("userInfo") public UserInfoOutputDto userInfo(
        @RequestHeader("Authorization") @JwtTokenConstraint final String authorization,
        final Principal principal) {
        return this.authService.userInfo(principal);
    }

    @ExceptionHandler({Exception.class}) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private void exceptionHandler(final HttpServletRequest request,
        final HttpServletResponse response, final Exception ex) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "invalid credentials");
    }

    @ExceptionHandler({ConstraintViolationException.class}) @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void constraintViolationExceptionHandler(final HttpServletRequest request,
        final HttpServletResponse response, final Exception ex) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
