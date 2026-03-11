package com.cloudy.demo.api;

import com.cloudy.demo.api.dto.CreateTaskRequest;
import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.application.CreateTaskUseCase;
import com.cloudy.demo.application.GetTaskUseCase;
import com.cloudy.demo.domain.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskDtoMapper mapper;
    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;

    public TaskController(TaskDtoMapper mapper, CreateTaskUseCase createTaskUseCase, GetTaskUseCase getTaskUseCase) {
        this.mapper = mapper;
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable UUID id) {
        Task task = getTaskUseCase.getTask(id);
        return mapper.toResponse(task);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public TaskResponse create(@RequestBody CreateTaskRequest request) {
        Task task = createTaskUseCase.create(
                request.getTitle(),
                request.getDescription()
        );
        return mapper.toResponse(task);
    }

}
