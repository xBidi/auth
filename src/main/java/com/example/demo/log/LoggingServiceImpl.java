package com.example.demo.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration @Slf4j public class LoggingServiceImpl {

    @Bean LoggingService loggingService() {
        log.debug("new LoggingService()");
        return new LoggingService() {
            @Override public void logRequest(HttpServletRequest httpServletRequest, Object body) {
                log.debug("request url: " + httpServletRequest.getRequestURL().toString());
                log.debug("request body: " + body.toString().replace("\n", "").replace("\r", ""));
            }

            @Override public void logResponse(HttpServletRequest httpServletRequest,
                HttpServletResponse httpServletResponse, Object body) {
                log.debug("response status: " + httpServletResponse.getStatus());
                log.debug("response body: " + body.toString().replace("\n", "").replace("\r", ""));
            }
        };
    }
}
