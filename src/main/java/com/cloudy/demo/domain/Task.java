package com.cloudy.demo.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Task {

    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private TaskStatus status;

    private Task() {}

    public static Task create(String title, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }

        Task task = new Task();
        task.id = UUID.randomUUID();
        task.title = title;
        task.description = description;
        task.createdAt = LocalDateTime.now();
        task.status = TaskStatus.OPEN;
        return task;
    }

    public static Task restore(UUID id, String title, String description, LocalDateTime createdAt, TaskStatus status) {
        Task task = new Task();
        task.id = id;
        task.title = title;
        task.description = description;
        task.createdAt = createdAt;
        task.status = status;
        return task;
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
