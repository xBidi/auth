package com.example.demo.log;

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
        log.debug("{preHandle start}");

        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name()) && request
            .getMethod().equals(HttpMethod.GET.name())) {
            loggingService.logRequest(request, null);
        }
        log.debug("{preHandle start}");
        return true;
    }
}