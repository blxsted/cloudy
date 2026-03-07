package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GetTaskUseCase {

    private final TaskRepository taskRepository;

    public GetTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTask(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Task ID darf nicht null sein");
        }

        return (taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task nicht gefunden")));
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
}
