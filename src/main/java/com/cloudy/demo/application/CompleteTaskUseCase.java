package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class CompleteTaskUseCase {

    private final TaskRepository taskRepository;

    public CompleteTaskUseCase(TaskRepository taskRepository) { this.taskRepository = taskRepository; }

    @Transactional
    public void complete(UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task nicht vorhanden"));

        task.complete();
        taskRepository.save(task);
    }
}
