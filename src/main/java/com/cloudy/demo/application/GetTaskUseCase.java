package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GetTaskUseCase {

    private final TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(GetTaskUseCase.class);


    public GetTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTask(UUID taskId) {
        log.info("Getting Task ID {}", taskId);

        if (taskId == null) {
            log.error("Task ID darf nicht null sein");
            throw new IllegalArgumentException("Task ID darf nicht null sein");
        }

        return (taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task {} not found", taskId);
                    return new TaskNotFoundException(taskId);
                }));
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }
}
