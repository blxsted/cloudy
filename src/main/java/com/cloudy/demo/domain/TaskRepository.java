package com.cloudy.demo.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    Task save(Task task);

    boolean deleteById(UUID id);

}
