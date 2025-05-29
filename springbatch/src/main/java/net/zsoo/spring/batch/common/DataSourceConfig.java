package net.zsoo.spring.batch.common;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
@PropertySource("classpath:properties/jdbc.properties")
public class DataSourceConfig {
    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        Class<?> driverClass = Class.forName(env.getProperty("jdbc.driverClassName"));
        dataSource.setDriverClass((Class<? extends Driver>) driverClass);
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        return dataSource;
    }
}
