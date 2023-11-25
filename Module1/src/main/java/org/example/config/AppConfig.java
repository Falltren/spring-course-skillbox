package org.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan("org.example")
@PropertySource("classpath:application.properties")
public class AppConfig {

}
