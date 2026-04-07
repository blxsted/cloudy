package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(CreateTaskUseCase.class);

    public CreateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(String title, String description) {
        if (description == null || description.isEmpty()) {
            log.warn("Description is null, creating task with empty description");
            description = "";
        }

        log.info("Creating task with title: {}", title);
        Task task = Task.create(title, description);
        Task savedTask = taskRepository.save(task);
        log.info("Created task with id: {}", savedTask.getId());
        return savedTask;
    }

}
