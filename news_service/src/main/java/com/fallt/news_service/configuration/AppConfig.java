package com.fallt.news_service.configuration;

import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.service.AccessCheckerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    public Map<CheckType, AccessCheckerService> accessCheckerServices(Collection<AccessCheckerService> checkerServices) {
        return checkerServices.stream().collect(Collectors.toMap(AccessCheckerService::getType, Function.identity()));
    }
}
