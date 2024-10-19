package com.fallt.task_tracker.controller;

import com.fallt.task_tracker.dto.TaskFullRs;
import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.TaskSimpleRs;
import com.fallt.task_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public Mono<TaskFullRs> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping
    public Flux<TaskFullRs> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskSimpleRs> createTask(@RequestBody TaskRq request) {
        return taskService.createTask(request);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public Mono<TaskSimpleRs> updateTask(@PathVariable String id, @RequestBody TaskRq request) {
        return taskService.updateTask(id, request);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @PatchMapping("/{id}")
    public Mono<Void> addObserver(@PathVariable String id, @RequestParam String observer) {
        return taskService.addObserver(id, observer);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTaskById(@PathVariable String id) {
        return taskService.deleteTaskById(id);
    }
}
