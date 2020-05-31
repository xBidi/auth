package com.spring.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class DefaultExceptionHandler {

  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private void exceptionHandler(
      HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
    response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
  }

  @ExceptionHandler({AccessDeniedException.class})
  @ResponseStatus(HttpStatus.FORBIDDEN)
  private void accessDeniedExceptionHandler(
      final HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
      throws IOException {
    response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
  }
}
