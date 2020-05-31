package com.spring.server.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.Principal;

/**
 * Swagger-ui endpoint configuration
 *
 * @author diegotobalina
 */
@Configuration @EnableSwagger2 @Profile("dev") @Slf4j public class SwaggerConfig {
    @Bean public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).
            useDefaultResponseMessages(false).
            ignoredParameterTypes(Principal.class).
            select().
            apis(RequestHandlerSelectors.basePackage("com.spring.server.controller")).
            paths(PathSelectors.any()).
            build();
    }

}
