package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;

import java.util.*;

public class FakeTaskRepository implements TaskRepository {

    private final Map<UUID, Task> tasks = new HashMap<>();

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public boolean deleteById(UUID id) {
        return tasks.remove(id) != null;
    }
}
