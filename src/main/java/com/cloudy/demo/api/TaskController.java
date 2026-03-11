package com.cloudy.demo.api;

import com.cloudy.demo.api.dto.CreateTaskRepuest;
import com.cloudy.demo.api.dto.TaskResponse;
import com.cloudy.demo.application.CreateTaskUseCase;
import com.cloudy.demo.application.GetTaskUseCase;
import com.cloudy.demo.domain.Task;
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

    @PostMapping()
    public TaskResponse create(@RequestBody CreateTaskRepuest request) {
        Task task = createTaskUseCase.create(
                request.getTitle(),
                request.getDescription()
        );
        return mapper.toResponse(task);
    }

}
