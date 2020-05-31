package com.spring.server.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public abstract class RequestUtil {

  public static boolean isApiRequest(final ServletRequest req) {
    if (!(req instanceof HttpServletRequest)) {
      return false;
    }
    var requestUrl = ((HttpServletRequest) req).getRequestURL();
    var requestUrlString = requestUrl.toString();
      return requestUrlString.contains("/api/");
  }
}
