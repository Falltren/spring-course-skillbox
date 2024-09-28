package com.fallt.task_tracker.dto;

import com.fallt.task_tracker.entity.TaskStatus;
import lombok.Data;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
public class TaskSimpleRs implements TaskResponse {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    @BsonRepresentation(BsonType.STRING)
    private TaskStatus status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds = new HashSet<>();
}
