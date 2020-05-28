package com.spring.server.controller;

import com.spring.server.model.dto.*;
import com.spring.server.model.validator.JwtTokenConstraint;
import com.spring.server.model.validator.TokenConstraint;
import com.spring.server.service.AuthService;
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
@RestController @RequestMapping("api/v1/oauth2")
@ResponseStatus(HttpStatus.OK) @Slf4j @Validated public class AuthController {

    @Autowired AuthService authService;


    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Invalid credentials")})
    @ApiOperation(value = "Login", notes = "") @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("login")
    public LoginOutputDto login(@RequestBody @Valid LoginInputDto loginInputDto) throws Exception {
        return this.authService.login(loginInputDto);
    }

    @ApiOperation(value = "Logout", notes = "") @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("logout") public void logout(@RequestBody @Valid LogoutInputDto logoutInputDto) {
        this.authService.logout(logoutInputDto);
    }

    @ApiOperation(value = "Access", notes = "") @ResponseStatus(HttpStatus.OK)
    @PostMapping("access")
    public AccessOutputDto access(@RequestBody @Valid AccessInputDto accessInputDto)
        throws Exception {
        return this.authService.access(accessInputDto);
    }

    @ApiOperation(value = "Token info", notes = "Returns token information, valid tokens: Bearer, Jwt")
    @ResponseStatus(HttpStatus.OK) @GetMapping("tokenInfo")
    public TokenInfoOutputDto tokenInfo(@RequestParam(name = "token") @TokenConstraint String token)
        throws Exception {
        return this.authService.tokenInfo(token);
    }

    @ApiOperation(value = "User info", notes = "") @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @GetMapping("userInfo") public UserInfoOutputDto userInfo(
        @RequestHeader("Authorization") @JwtTokenConstraint String authorization,
        Principal principal) throws Exception {
        return this.authService.findByPrincipal(principal);
    }

    @ExceptionHandler({Exception.class}) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private void exceptionHandler(HttpServletRequest request, HttpServletResponse response,
        Exception ex) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "invalid credentials");
    }

    @ExceptionHandler({ConstraintViolationException.class}) @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void constraintViolationExceptionHandler(HttpServletRequest request,
        HttpServletResponse response, Exception ex) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
