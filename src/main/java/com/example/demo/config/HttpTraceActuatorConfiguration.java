package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("dev") @Slf4j public class HttpTraceActuatorConfiguration {

    @Bean public HttpTraceRepository httpTraceRepository() {
        log.debug("Createing HttpTraceRepository bean");
        return new InMemoryHttpTraceRepository();
    }

}
