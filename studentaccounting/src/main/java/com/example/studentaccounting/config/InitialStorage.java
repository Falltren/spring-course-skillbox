package com.example.studentaccounting.config;

import com.example.studentaccounting.service.StudentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "app", name = "storage", havingValue = "init")
@Component
@RequiredArgsConstructor
public class InitialStorage {

    private final StudentService service;

    @EventListener(ApplicationStartedEvent.class)
    public void initStorage() {
        service.addStudent("Jack", "Reacher", 35);
        service.addStudent("Jason", "Bourne", 32);
        service.addStudent("Eon", "Flux", 27);
    }
}
