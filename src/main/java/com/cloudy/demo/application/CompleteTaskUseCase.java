package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CompleteTaskUseCase {

    private final TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(CompleteTaskUseCase.class);

    public CompleteTaskUseCase(TaskRepository taskRepository) { this.taskRepository = taskRepository; }

    @Transactional
    public void complete(UUID taskId) {
        log.info("Completing task {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task {} not found", taskId);
                    return new TaskNotFoundException(taskId);
                });

        log.debug("Task {} current status: {}", taskId, task.getStatus());
        task.complete();
        log.debug("Task {} new status: {}", taskId, task.getStatus());
        taskRepository.save(task);
        log.info("Task {} completed successfully", taskId);
    }
}
