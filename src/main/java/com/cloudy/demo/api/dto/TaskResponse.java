package com.cloudy.demo.api.dto;

import com.cloudy.demo.domain.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private TaskStatus status;

    public TaskResponse(UUID id, String title, String description, LocalDateTime createdAt, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
