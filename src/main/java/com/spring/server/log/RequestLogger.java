package com.spring.server.log;

import com.spring.server.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component @Slf4j public class RequestLogger implements HandlerInterceptor {

    @Autowired LoggingService loggingService;

    @Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && request
            .getMethod().equals(HttpMethod.GET.name())) {
            loggingService.logRequest(request, null);
        }
        return true;
    }
}
