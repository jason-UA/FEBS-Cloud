package com.febs.server.system.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:febs-server-system.properties"})
@ConfigurationProperties(prefix = "febs.server.system")
public class FebsServerSystemProperties {

    private String anonUrl;

    private FebsSwaggerProperties swagger = new FebsSwaggerProperties();
}