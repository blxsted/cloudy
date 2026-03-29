package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class StartTaskUseCase {

    private final TaskRepository taskRepository;

    public StartTaskUseCase(TaskRepository taskRepository) { this.taskRepository = taskRepository; }

    @Transactional
    public void start(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task nicht vorhanden.")) ;

        task.start();
        taskRepository.save(task);
    }
}
