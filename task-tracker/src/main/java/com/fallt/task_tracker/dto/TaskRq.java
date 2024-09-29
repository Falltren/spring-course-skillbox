package com.fallt.task_tracker.dto;

import com.fallt.task_tracker.entity.TaskStatus;
import lombok.Data;

import java.util.Set;

@Data
public class TaskRq {

    private String name;

    private String description;

    private TaskStatus status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

}
