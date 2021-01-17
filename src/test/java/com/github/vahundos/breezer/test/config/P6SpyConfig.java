package com.github.vahundos.breezer.test.config;

import com.p6spy.engine.spy.P6DataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "p6spy.enable", havingValue = "true")
public class P6SpyConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        JDBCDataSource hsqldbDataSource = new JDBCDataSource();
        hsqldbDataSource.setUrl(dataSourceProperties.getUrl());
        hsqldbDataSource.setUser(dataSourceProperties.getUsername());
        hsqldbDataSource.setPassword(dataSourceProperties.getPassword());
        return new P6DataSource(hsqldbDataSource);
    }
}
