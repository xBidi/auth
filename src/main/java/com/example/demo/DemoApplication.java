package com.example.demo;

import com.example.demo.log.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Application main
 *
 * @author diegotobalina
 */
@SpringBootApplication @Slf4j public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Bean LoggingService loggingService() {
        return new LoggingService() {
            @Override public void logRequest(HttpServletRequest httpServletRequest, Object body) {
                log.debug(body.toString());
            }

            @Override public void logResponse(HttpServletRequest httpServletRequest,
                HttpServletResponse httpServletResponse, Object body) {
                log.debug(body.toString());
            }
        };
    }
}
