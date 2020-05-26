package com.example.demo;

import com.example.demo.log.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Application main
 *
 * @author diegotobalina
 */
@SpringBootApplication @Slf4j @EnableAsync public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean @Autowired @ConditionalOnExpression("'${mongo.transactions}'=='enabled'")
    MongoTransactionManager mongoTransactionManager(MongoDbFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean LoggingService loggingService() {
        log.debug("{loggingService bean} creating new bean");
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
