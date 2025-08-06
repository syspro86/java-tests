package net.zsoo.spring.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@PropertySource("classpath:properties/sample.properties")
@Configuration
@Getter
public class SampleProperties {

    @Value("${sample.name}")
    private String name;
    @Value("${sample.description}")
    private String description;
    @Value("${sample.version}")
    private String version;
}
