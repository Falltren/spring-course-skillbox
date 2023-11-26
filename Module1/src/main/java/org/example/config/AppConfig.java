package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("org.example")
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class AppConfig {

}
