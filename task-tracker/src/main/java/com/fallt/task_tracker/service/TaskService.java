package com.fallt.task_tracker.service;

import com.fallt.task_tracker.dto.TaskResponse;
import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.UserDto;
import com.fallt.task_tracker.entity.Task;
import com.fallt.task_tracker.entity.User;
import com.fallt.task_tracker.mapper.TaskMapper;
import com.fallt.task_tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    public Mono<TaskResponse> getTaskById(String id) {
        Mono<Task> existedTask = taskRepository.findById(id);
        Mono<UserDto> author = existedTask.flatMap(task -> userService.getUserById(task.getAuthorId()));
        Mono<UserDto> assignee = existedTask.flatMap(task -> userService.getUserById(task.getAssigneeId()));
        Flux<User> observers = existedTask.flatMapMany(task -> userService.getUsersBySetId(task.getObserverIds()));

        return existedTask.map(TaskMapper.INSTANCE::toDto).zipWith(author, (task, authorInfo) -> {
                    task.setAuthorId(authorInfo);
                    return task;
                })
                .zipWith(assignee, (task, assigneeInfo) -> {
                    task.setAssigneeId(assigneeInfo);
                    return task;
                });
//                .zipWith(observers, (task, observersInfo) -> {
//                    task.setObserverIds(observers.flatMap());
//                })
    }

    public Mono<TaskResponse> createTask(TaskRq request) {
        return taskRepository.save(TaskMapper.INSTANCE.toEntity(request))
                .map(TaskMapper.INSTANCE::toSimpleDto);
    }

}

