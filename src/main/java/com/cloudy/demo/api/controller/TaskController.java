package com.cloudy.demo.api.controller;

import com.cloudy.demo.api.mapper.TaskResponseMapper;
import com.cloudy.demo.api.dto.CreateTaskRequest;
import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.application.CreateTaskUseCase;
import com.cloudy.demo.application.GetTaskUseCase;
import com.cloudy.demo.domain.Task;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskResponseMapper mapper;
    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;

    public TaskController(TaskResponseMapper mapper, CreateTaskUseCase createTaskUseCase, GetTaskUseCase getTaskUseCase) {
        this.mapper = mapper;
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable UUID id) {
        return mapper.toResponse(getTaskUseCase.getTask(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public TaskResponse create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.create(
                request.getTitle(),
                request.getDescription()
        );

        return mapper.toResponse(task);
    }

    @GetMapping
    public List<TaskResponse> getTasks() {
        return mapper.toResponseList(getTaskUseCase.getTasks());
    }
}
