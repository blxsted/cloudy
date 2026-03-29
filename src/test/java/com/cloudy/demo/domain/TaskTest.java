package com.cloudy.demo.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class TaskTest {

    @Test
    void shouldCreateValidTask() {
        String title = "Learn Spring";
        String desc = "description";

        Task task = Task.create(title, desc);

        assertThat(task.getId()).isNotNull();
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(desc);
        assertThat(task.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldRejectNullTitle() {

        assertThatThrownBy(() ->
                Task.create(null, "description"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectBlankTitle() {
        assertThatThrownBy(() ->
                Task.create("", "description"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must not be empty");
    }

    @Test
    void shouldSetStatusOpen() {
        String title = "Learn Spring";

        Task task = Task.create(title, "description");

        assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);
    }

    @Test
    void shouldRestoreTaskFromPersistence() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        Task task = Task.restore(
                id,
                "Learn Spring",
                "description",
                createdAt,
                TaskStatus.IN_PROGRESS
        );

        LocalDateTime after = LocalDateTime.now();

        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo("Learn Spring");
        assertThat(task.getDescription()).isEqualTo("description");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getCreatedAt())
                .isNotNull()
                .isEqualTo(createdAt)
                .isBeforeOrEqualTo(after);
    }

    @Test
    void shouldStartTask() {
        Task task = Task.create("Learn Spring", "desc");

        task.start();

        assertThat(task.getStatus())
                .isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldCompleteTask() {
        Task task = Task.create("Learn Spring", "desc");

        task.start();
        task.complete();

        assertThat(task.getStatus())
                .isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    void shouldReopenCompletedTask(){
        Task task = Task.create("Learn Spring", "desc");

        task.start();
        task.complete();
        task.reopen();

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldNotCompleteIfOpen(){
        Task task = Task.create("Learn Spring", "desc");

        assertThatThrownBy(task::complete).isInstanceOf(IllegalStateException.class)
                .hasMessage("Task kann nicht abgeschlossen werden.");
    }

    @Test
    void shouldNotStartIfAlreadyInProgress(){
        Task task = Task.create("Learn Spring", "desc");

        task.start();
        assertThatThrownBy(task::start)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Task kann nicht gestartet werden.");
    }

    @Test
    void shouldNotCompleteIfAlreadyCompleted() {
        Task task = Task.create("Learn Spring", "desc");
        task.start();
        task.complete();

        assertThatThrownBy(task::complete)
                .isInstanceOf(IllegalStateException.class);
    }
}

