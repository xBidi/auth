package com.spring.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author diegotobalina
 */
@Configuration @Profile("dev") @Slf4j public class HttpTraceActuatorConfiguration {

    @Bean public HttpTraceRepository httpTraceRepository() {
        log.info("new InMemoryHttpTraceRepository()");
        return new InMemoryHttpTraceRepository();
    }

}
