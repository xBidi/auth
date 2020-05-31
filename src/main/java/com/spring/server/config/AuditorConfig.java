package com.spring.server.config;

import com.spring.server.service.AuditorAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
@Slf4j
public class AuditorConfig {
  @Bean
  public org.springframework.data.domain.AuditorAware auditorAware() {
    return new AuditorAware();
  }
}
