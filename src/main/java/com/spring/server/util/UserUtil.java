package com.spring.server.util;

import com.spring.server.model.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public abstract class UserUtil {

  public static User getUserFromPrincipal(Principal principal) {
    UsernamePasswordAuthenticationToken authenticationToken =
        (UsernamePasswordAuthenticationToken) principal;
    return (User) authenticationToken.getPrincipal();
  }

  public static String getUserIdFromPrincipal(Principal principal) {
    return getUserFromPrincipal(principal).getId();
  }
}
