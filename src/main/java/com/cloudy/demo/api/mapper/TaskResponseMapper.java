package com.cloudy.demo.api.mapper;

import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.domain.Task;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskResponseMapper {

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getStatus()
        );
    }

    public List<TaskResponse> toResponseList(List<Task> tasks) {
        if (tasks == null) return List.of();

        return tasks.stream().map(this::toResponse).toList();
    }

}
