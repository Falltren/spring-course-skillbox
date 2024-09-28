package com.fallt.task_tracker.service;

import com.fallt.task_tracker.dto.TaskFullRs;
import com.fallt.task_tracker.dto.TaskResponse;
import com.fallt.task_tracker.dto.TaskRq;
import com.fallt.task_tracker.dto.UserDto;
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

    public Mono<TaskResponse> getTaskById(String id) {
        Mono<Task> existedTask = findTaskById(id);
        Mono<UserDto> author = existedTask.flatMap(task -> userService.getUserById(task.getAuthorId()));
        Mono<UserDto> assignee = existedTask.flatMap(task -> userService.getUserById(task.getAssigneeId()));
        Flux<UserDto> observers = existedTask.flatMapMany(
                task -> userService.getUsersBySetId(task.getObserverIds())).map(UserMapper.INSTANCE::toDto);
        return existedTask.map(TaskMapper.INSTANCE::toFullDto)
                .zipWith(author, (task, authorInfo) -> {
                    task.setAuthorId(authorInfo);
                    return task;
                })
                .zipWith(assignee, (task, assigneeInfo) -> {
                    task.setAssigneeId(assigneeInfo);
                    return task;
                })
                .zipWith(observers.collectList(), (task, observersList) -> {
                    task.setObserverIds(new HashSet<>(observersList));
                    return task;
                });
    }

    public Flux<TaskResponse> getAllTasks() {
        return taskRepository.findAll().map(TaskMapper.INSTANCE::toFullDto);
    }

//    public Flux<TaskResponse> getAllTasks() {
//        return taskRepository.findAll()
//                .flatMap(task -> {
//                    Mono<UserDto> author = userService.getUserById(task.getAuthorId());
//                    Mono<UserDto> assignee = userService.getUserById(task.getAssigneeId());
//                    Flux<UserDto> observers = userService.getUsersBySetId(task.getObserverIds()).map(UserMapper.INSTANCE::toDto);
//
//                    return Mono.zip(Mono.just(task), author, assignee, observers.collectList()).map(t -> {
//                        TaskFullRs taskDto = TaskMapper.INSTANCE.toFullDto(task);
//                        taskDto.setAuthorId(t.getT2());
//                        taskDto.setAssigneeId(t.getT3());
//                        taskDto.setObserverIds(new HashSet<>(t.getT4()));
//                        return taskDto;
//                    });
//                });
//    }

    public Mono<TaskResponse> createTask(TaskRq request) {
        return taskRepository.save(TaskMapper.INSTANCE.toEntity(request))
                .map(TaskMapper.INSTANCE::toSimpleDto);
    }

    public Mono<TaskResponse> updateTask(String id, TaskRq request) {
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

