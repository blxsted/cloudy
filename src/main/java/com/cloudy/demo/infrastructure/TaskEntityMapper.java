package com.cloudy.demo.infrastructure;

import com.cloudy.demo.domain.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityMapper {

    public TaskJpaEntity toJpaEntity(Task task) {
        return new TaskJpaEntity(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getStatus());
    }

    public Task toDomain(TaskJpaEntity entity) {
        return Task.restore(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getStatus()
        );
    }
}
