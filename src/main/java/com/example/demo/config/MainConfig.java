package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * App data source configuration
 *
 * @author diegotobalina
 */
@Configuration @Slf4j public class MainConfig {

    @Value("${spring.datasource.url}") private String databaseUri;
    @Value("${spring.datasource.ssl}") private String dataSourceSsl;

    @Bean public DataSource dataSource() throws URISyntaxException {
        log.debug("data source configuration (databaseUri): " + databaseUri);
        log.debug("data source configuration (dataSourceSsl): " + dataSourceSsl);
        String jdbcPrefix = "jdbc:postgresql://";
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        if (!databaseUri.startsWith(jdbcPrefix)) {
            URI dbUri = new URI(databaseUri);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl =
                jdbcPrefix + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode="
                    + this.dataSourceSsl;
            dataSourceBuilder.url(dbUrl);
            dataSourceBuilder.username(username);
            dataSourceBuilder.password(password);
        } else {
            dataSourceBuilder.url(databaseUri);
        }
        return dataSourceBuilder.build();
    }
}
