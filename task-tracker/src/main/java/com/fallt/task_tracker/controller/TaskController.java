package com.fallt.task_tracker.controller;

import com.fallt.task_tracker.dto.TaskResponse;
import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public Mono<TaskResponse> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public Flux<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> createTask(@RequestBody TaskRq request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public Mono<TaskResponse> updateTask(@PathVariable String id, @RequestBody TaskRq request) {
        return taskService.updateTask(id, request);
    }

    @PatchMapping("/{id}")
    public Mono<Void> addObserver(@PathVariable String id, @RequestParam String observer) {
        return taskService.addObserver(id, observer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTaskById(@PathVariable String id) {
        return taskService.deleteTaskById(id);
    }
}
