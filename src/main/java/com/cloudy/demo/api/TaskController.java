package com.cloudy.demo.api;

import com.cloudy.demo.application.CreateTaskUseCase;
import com.cloudy.demo.application.GetTaskUseCase;
import com.cloudy.demo.domain.Task;

import java.util.List;
import java.util.UUID;

public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase, GetTaskUseCase getTaskUseCase) {
        this.createTaskUseCase =  createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
    }

    public Task createTask(String title, String description) {
        return createTaskUseCase.create(title, description);
    }

    public Task getTaskById(UUID id) {
        return getTaskUseCase.getTask(id);
    }

    public List<Task> getTasks() {
        return getTaskUseCase.getTasks();
    }
}
