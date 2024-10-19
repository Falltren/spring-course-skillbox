package com.fallt.task_tracker.dto;

import com.fallt.task_tracker.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRq {

    private String name;

    private String description;

    private TaskStatus status;

    private String assigneeId;

    private Set<String> observerIds;

}
