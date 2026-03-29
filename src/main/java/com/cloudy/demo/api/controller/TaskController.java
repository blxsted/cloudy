package com.cloudy.demo.api.controller;

import com.cloudy.demo.api.mapper.TaskResponseMapper;
import com.cloudy.demo.api.dto.CreateTaskRequest;
import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.application.*;
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
    private final StartTaskUseCase startTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final ReopenTaskUseCase reopenTaskUseCase;

    public TaskController(TaskResponseMapper mapper, CreateTaskUseCase createTaskUseCase, GetTaskUseCase getTaskUseCase, StartTaskUseCase startTaskUseCase, CompleteTaskUseCase completeTaskUseCase, ReopenTaskUseCase reopenTaskUseCase) {
        this.mapper = mapper;
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.startTaskUseCase = startTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.reopenTaskUseCase = reopenTaskUseCase;
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/start")
    public void startTask(@PathVariable UUID id) {
        startTaskUseCase.start(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/complete")
    public void completeTask(@PathVariable UUID id) {
        completeTaskUseCase.complete(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/reopen")
    public void reopenTask(@PathVariable UUID id) {
        reopenTaskUseCase.reopen(id);
    }
}
