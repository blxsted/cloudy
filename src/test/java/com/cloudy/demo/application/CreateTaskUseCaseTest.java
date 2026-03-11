package com.cloudy.demo.application;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import com.cloudy.demo.domain.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class CreateTaskUseCaseTest {

    @Test
    void shouldCreateTaskAndSaveIt() {

        TaskRepository repository =  new FakeTaskRepository();
        CreateTaskUseCase useCase =  new CreateTaskUseCase(repository);

        Task task = useCase.create("Learn Spring", "description");

        assertThat(task).isNotNull();
        assertThat(task.getId()).isNotNull();
        assertThat(task.getTitle()).isEqualTo("Learn Spring");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);

    }
}