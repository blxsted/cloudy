package com.cloudy.demo.application;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task nicht gefunden:" + id);
    }
}
