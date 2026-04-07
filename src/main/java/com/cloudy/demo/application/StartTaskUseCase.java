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
public class StartTaskUseCase {

    private final TaskRepository taskRepository;

    private static final Logger log = LoggerFactory.getLogger(StartTaskUseCase.class);

    public StartTaskUseCase(TaskRepository taskRepository) { this.taskRepository = taskRepository; }

    @Transactional
    public void start(UUID taskId) {
        log.info("Starting Task {}", taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task {} not found", taskId);
                    return new TaskNotFoundException(taskId);
                });

        task.start();

        taskRepository.save(task);
        log.info("Task {} started", taskId);

    }
}
