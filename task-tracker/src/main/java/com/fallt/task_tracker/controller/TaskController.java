package com.fallt.task_tracker.controller;

import com.fallt.task_tracker.dto.TaskResponse;
import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.TaskRs;
import com.fallt.task_tracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public Mono<TaskResponse> createTask(@RequestBody TaskRq request) {
        return taskService.createTask(request);
    }
}
