package com.fallt.task_tracker.dto;

import com.fallt.task_tracker.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFullRs {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus status;

    private UserRs author;

    private UserRs assignee;

    @Builder.Default
    private Set<UserRs> observers = new HashSet<>();
}
