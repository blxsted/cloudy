package com.cloudy.demo.api.controller;

import com.cloudy.demo.api.mapper.TaskResponseMapper;
import com.cloudy.demo.api.dto.CreateTaskRequest;
import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.application.*;
import com.cloudy.demo.domain.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tasks", description = "Task Management API")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskResponseMapper mapper;
    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final StartTaskUseCase startTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;
    private final ReopenTaskUseCase reopenTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskController(TaskResponseMapper mapper, CreateTaskUseCase createTaskUseCase, GetTaskUseCase getTaskUseCase, StartTaskUseCase startTaskUseCase, CompleteTaskUseCase completeTaskUseCase, ReopenTaskUseCase reopenTaskUseCase, DeleteTaskUseCase deleteTaskUseCase) {
        this.mapper = mapper;
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.startTaskUseCase = startTaskUseCase;
        this.completeTaskUseCase = completeTaskUseCase;
        this.reopenTaskUseCase = reopenTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    @Operation(summary = "Get a task by ID", description = "Retrieve a single task by its UUID")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found")})
    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable UUID id) {
        return mapper.toResponse(getTaskUseCase.getTask(id));
    }

    @Operation(summary = "Create a new task", description = "Create a new task with title and optional description")
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Invalid input - title is required")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public TaskResponse create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.create(
                request.getTitle(),
                request.getDescription()
        );

        return mapper.toResponse(task);
    }

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<TaskResponse> getTasks() {
        return mapper.toResponseList(getTaskUseCase.getTasks());
    }

    @Operation(summary = "Start a task", description = "Change task status from PENDING to IN_PROGRESS")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/start")
    public void startTask(@PathVariable UUID id) {
        startTaskUseCase.start(id);
    }

    @Operation(summary = "Complete a task", description = "Change task status to COMPLETED")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/complete")
    public void completeTask(@PathVariable UUID id) {
        completeTaskUseCase.complete(id);
    }

    @Operation(summary = "Reopen a task", description = "Change task status from COMPLETED back to IN_PROGRESS")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/reopen")
    public void reopenTask(@PathVariable UUID id) {
        reopenTaskUseCase.reopen(id);
    }

    @Operation(summary = "Delete a task", description = "Delete a task by its UUID")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Task not found")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        deleteTaskUseCase.delete(id);
    }
}
