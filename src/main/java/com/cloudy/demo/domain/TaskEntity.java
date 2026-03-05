package com.cloudy.demo.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskEntity {

    private final UUID id;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final TaskStatus status;

    public TaskEntity(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }

        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = TaskStatus.OPEN;
    }

    public UUID getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public TaskStatus getStatus(){
        return status;
    }
}
