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
        log.debug("{start login} (loginInputDto):" + loginInputDto.toString());
        LoginOutputDto login = this.authService.login(loginInputDto);
        log.debug("{end login} (login):" + login.toString());
        return login;
    }

    @ApiOperation(value = "Logout", notes = "") @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("logout") public void logout(@RequestBody @Valid LogoutInputDto logoutInputDto)
        throws Exception {
        log.debug("{start logout} (logoutInputDto):" + logoutInputDto.toString());
        this.authService.logout(logoutInputDto);
        log.debug("{end logout}");
    }

    @ApiOperation(value = "Access", notes = "") @ResponseStatus(HttpStatus.OK)
    @PostMapping("access")
    public AccessOutputDto access(@RequestBody @Valid AccessInputDto accessInputDto)
        throws Exception {
        log.debug("{start access} (accessInputDto):" + accessInputDto.toString());
        AccessOutputDto access = this.authService.access(accessInputDto);
        log.debug("{end access} (access):" + access.toString());
        return access;
    }

    @ApiOperation(value = "Token info", notes = "Returns token information, valid tokens: Bearer, Jwt")
    @ResponseStatus(HttpStatus.OK) @PostMapping("tokenInfo")
    public TokenInfoOutputDto tokenInfo(@RequestBody @Valid TokenInfoInputDto tokenInfoInputDto)
        throws Exception {
        log.debug("{start tokenInfo} (tokenInfoInputDto):" + tokenInfoInputDto.toString());
        TokenInfoOutputDto tokenInfoOutputDto = this.authService.tokenInfo(tokenInfoInputDto);
        log.debug("{end tokenInfo} (tokenInfoOutputDto):" + tokenInfoOutputDto.toString());
        return tokenInfoOutputDto;
    }

    @ApiOperation(value = "User info", notes = "") @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @PostMapping("userInfo") public UserInfoOutputDto userInfo(Principal principal)
        throws Exception {
        log.debug("{start userInfo} (principal):" + principal.toString());
        UserInfoOutputDto userInfoOutputDto = this.authService.findByPrincipal(principal);
        log.debug("{end  userInfo} (userInfoOutputDto):" + userInfoOutputDto.toString());
        return userInfoOutputDto;
    }

    @ExceptionHandler({Exception.class}) @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private void exceptionHandler(HttpServletRequest request, HttpServletResponse response,
        Exception ex) throws IOException {
        log.debug("{start exceptionHandler} (ex): " + ex.toString());
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "invalid credentials");
        log.debug("{end exceptionHandler}");
    }
}
