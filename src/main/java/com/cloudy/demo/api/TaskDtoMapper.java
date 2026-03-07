package com.cloudy.demo.api;

import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.domain.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoMapper {

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getStatus()
        );
    }

}
