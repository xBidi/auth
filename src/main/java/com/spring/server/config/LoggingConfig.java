package com.spring.server.config;

import com.spring.server.service.LoggingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
  @Bean
  LoggingService loggingService() {
    return new LoggingService();
  }
}
