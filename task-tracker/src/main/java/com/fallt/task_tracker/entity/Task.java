package com.fallt.task_tracker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String id;

    private String name;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private TaskStatus status;

    private String authorId;

    private String assigneeId;

    private Set<String> observerIds;

    @ReadOnlyProperty
    private User user;

    @ReadOnlyProperty
    private User assigneeUser;

    @ReadOnlyProperty
    private Set<User> observers;
}
