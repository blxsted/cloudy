package com.cloudy.demo.infrastructure;

import com.cloudy.demo.domain.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    public TaskJpaEntity() {}

    public TaskJpaEntity(UUID id, String title, String description, LocalDateTime createdAt, TaskStatus status) {
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
