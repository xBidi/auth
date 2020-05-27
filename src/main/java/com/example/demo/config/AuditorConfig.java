package com.example.demo.config;

import com.example.demo.service.AuditorAwareImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration @EnableMongoAuditing(auditorAwareRef = "auditorAware") @Slf4j
public class AuditorConfig {
    @Bean public AuditorAware<String> auditorAware() {
        log.info("new AuditorAwareImpl()");
        return new AuditorAwareImpl();
    }
}
