package com.fallt.task_tracker.service;

import com.fallt.task_tracker.dto.*;
import com.fallt.task_tracker.entity.Task;
import com.fallt.task_tracker.exception.EntityNotFoundException;
import com.fallt.task_tracker.mapper.TaskMapper;
import com.fallt.task_tracker.mapper.UserMapper;
import com.fallt.task_tracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    public Mono<TaskFullRs> getTaskById(String id) {
        return findTaskById(id)
                .flatMap(task -> {
                    Mono<UserRs> author = userService.getUserById(task.getAuthorId());
                    Mono<UserRs> assignee = userService.getUserById(task.getAssigneeId());
                    Flux<UserRs> observers = userService.getUsersBySetId(task.getObserverIds()).map(UserMapper.INSTANCE::toDto);
                    TaskFullRs taskDto = TaskMapper.INSTANCE.toFullDto(task);
                    return Mono.zip(Mono.just(task), author, assignee, observers.collectList()).map(t -> {
                        taskDto.setAuthor(t.getT2());
                        taskDto.setAssignee(t.getT3());
                        taskDto.setObservers(new HashSet<>(t.getT4()));
                        return taskDto;
                    });
                });

    }

    public Flux<TaskFullRs> getAllTasks() {
        return taskRepository.findAll()
                .flatMap(task -> {
                    Mono<UserRs> author = userService.getUserById(task.getAuthorId());
                    Mono<UserRs> assignee = userService.getUserById(task.getAssigneeId());
                    Flux<UserRs> observers = userService.getUsersBySetId(task.getObserverIds()).map(UserMapper.INSTANCE::toDto);
                    TaskFullRs taskDto = TaskMapper.INSTANCE.toFullDto(task);
                    return Mono.zip(Mono.just(task), author, assignee, observers.collectList()).map(t -> {
                        taskDto.setAuthor(t.getT2());
                        taskDto.setAssignee(t.getT3());
                        taskDto.setObservers(new HashSet<>(t.getT4()));
                        return taskDto;
                    });
                });
    }

    public Mono<TaskSimpleRs> createTask(TaskRq request) {
        Mono<String> userId = userService.getCurrentUserId();
        return userId.flatMap(currentUserId -> {
            Task task = TaskMapper.INSTANCE.toEntity(request);
            task.setAuthorId(currentUserId);
            return taskRepository.save(task)
                    .map(TaskMapper.INSTANCE::toSimpleDto);
        });
    }

    public Mono<TaskSimpleRs> updateTask(String id, TaskRq request) {
        return findTaskById(id)
                .flatMap(existedTask -> {
                    TaskMapper.INSTANCE.updateTaskFromDto(request, existedTask);
                    return taskRepository.save(existedTask)
                            .map(TaskMapper.INSTANCE::toSimpleDto);
                });
    }

    public Mono<Void> addObserver(String taskId, String observerId) {
        return findTaskById(taskId).map(task -> {
            task.getObserverIds().add(observerId);
            return task;
        }).flatMap(taskRepository::save).then();
    }

    public Mono<Void> deleteTaskById(String id) {
        return taskRepository.deleteById(id);
    }

    private Mono<Task> findTaskById(String id) {
        return taskRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(MessageFormat.format("Task with ID: {0} not found", id))));
    }

}

