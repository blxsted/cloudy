package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class GetTaskUseCaseTest {

    @Test
    void shouldReturnTaskById() {

        TaskRepository repository = new FakeTaskRepository();

        Task task = Task.create("Learn Spring", "description");
        repository.save(task);

        GetTaskUseCase useCase = new GetTaskUseCase(repository);

        Task result = useCase.getTask(task.getId());
        assertThat(result.getId()).isEqualTo(task.getId());
    }

    @Test
    void shouldThrowIfTaskNotFound() {

        TaskRepository repository = new FakeTaskRepository();
        GetTaskUseCase useCase = new GetTaskUseCase(repository);

        assertThatThrownBy(() ->
                useCase.getTask(UUID.randomUUID()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Task nicht gefunden");
    }
}
