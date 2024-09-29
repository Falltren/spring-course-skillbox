package com.fallt.task_tracker.dto;

import com.fallt.task_tracker.entity.TaskStatus;
import lombok.Data;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class TaskFullRs implements TaskResponse {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus status;

    private UserDto author;

    private UserDto assignee;

    private Set<UserDto> observers = new HashSet<>();
}
