package net.zsoo.spring.properties2;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import lombok.Getter;

@PropertySource("classpath:properties/sample.properties")
@Configuration
@Getter
public class Sample2Properties {

    @Autowired
    private Environment env;

    private String name;
    private String description;
    private String version;

    @PostConstruct
    public void init() {
        this.name = env.getProperty("sample.name");
        this.description = env.getProperty("sample.description");
        this.version = env.getProperty("sample.version");
    }
}
