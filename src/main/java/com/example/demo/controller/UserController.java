package com.example.demo.controller;

import com.example.demo.model.dto.RegisterInputDto;
import com.example.demo.model.dto.RegisterOutputDto;
import com.example.demo.model.dto.UserInfoOutputDto;
import com.example.demo.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * User endpoints
 *
 * @author diegotobalina
 */
@RestController @RequestMapping("${server.context.path}/${server.api.version}/${server.path.users}")
@ResponseStatus(HttpStatus.OK) @Slf4j public class UserController {

    @Autowired UserService userService;

    @ApiOperation(value = "Register a new user in the database", notes = "")
    @ResponseStatus(HttpStatus.CREATED) @PostMapping
    public RegisterOutputDto register(@RequestBody @Valid RegisterInputDto registerInputDto)
        throws Exception {
        return this.userService.register(registerInputDto);
    }

    @ApiOperation(value = "list all database users", notes = "ONLY ADMINS WITH READ+ SCOPE")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "jwt", dataType = "string", paramType = "header", required = true)})
    @GetMapping @PreAuthorize("hasRole('ADMIN') and hasPermission('hasAccess','READ')")
    public List<UserInfoOutputDto> users() {
        return userService.findAll();
    }

    @ExceptionHandler({Exception.class}) @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void exceptionHandler(HttpServletRequest request, HttpServletResponse response,
        Exception ex) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class}) @ResponseStatus(HttpStatus.FORBIDDEN)
    private void accessDeniedExceptionHandler(HttpServletRequest request,
        HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

}
