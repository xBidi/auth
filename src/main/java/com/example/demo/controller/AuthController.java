package com.example.demo.controller;

import com.example.demo.model.dto.*;
import com.example.demo.service.AuthService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

/**
 * Authorization endpoints
 *
 * @author diegotobalina
 */
@RestController @RequestMapping("${server.context.path}/${server.api.version}/${server.path.oauth}")
@ResponseStatus(HttpStatus.OK) @Slf4j public class AuthController {

    @Autowired AuthService authService;


    @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Invalid credentials")})
    @ApiOperation(value = "Login", notes = "") @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("login")
    public LoginOutputDto login(@RequestBody @Valid LoginInputDto loginInputDto) throws Exception {
        return this.authService.login(loginInputDto);
    }

    @ApiOperation(value = "Logout", notes = "") @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("logout") public void logout(@RequestBody @Valid LogoutInputDto logoutInputDto)
        throws Exception {
        this.authService.logout(logoutInputDto);
    }

    @ApiOperation(value = "Access", notes = "") @ResponseStatus(HttpStatus.OK)
    @PostMapping("access")
    public AccessOutputDto access(@RequestBody @Valid AccessInputDto accessInputDto)
        throws Exception {
        return this.authService.access(accessInputDto);
    }

    @ApiOperation(value = "Token info", notes = "Returns token information, valid tokens: Bearer, Jwt")
    @ResponseStatus(HttpStatus.OK) @PostMapping("tokenInfo")
    public TokenInfoOutputDto tokenInfo(@RequestBody @Valid TokenInfoInputDto tokenInfoInputDto)
        throws Exception {
        return this.authService.tokenInfo(tokenInfoInputDto);
    }

    @ApiOperation(value = "User info", notes = "") @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @PostMapping("userInfo") public UserInfoOutputDto userInfo(Principal principal)
        throws Exception {
        return this.authService.findByPrincipal(principal);
    }

    @ExceptionHandler({Exception.class}) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private void exceptionHandler(HttpServletRequest request, HttpServletResponse response,
        Exception ex) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "invalid credentials");
    }
}
