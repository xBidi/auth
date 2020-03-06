package com.example.demo;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Change database postgres -> h2 in memory for tests
 *
 * @author diegotobalina
 */
@Configuration public class MockDatabaseConfig {

    @Bean @Primary public DataSource getDataSource() {
        @SuppressWarnings("rawtypes") DataSourceBuilder dataSourceBuilder =
            DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.h2.Driver");
        dataSourceBuilder.url("jdbc:h2:mem:test");
        dataSourceBuilder.username("SA");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }
}
