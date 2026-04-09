package com.cloudy.demo.integration;

import com.cloudy.demo.api.dto.CreateTaskRequest;
import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "DELETE FROM tasks")
public class TaskControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    private String asJsonString(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private UUID createTask(String title, String description) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        request.setDescription(description);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(json).get("id").asText());
    }

    private String getStatus(UUID id) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/" + id))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readTree(json).get("status").asText();
    }

    private void changeStatus(String path) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .patch(path))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldCreateTaskViaHttp() throws Exception {
        UUID id = createTask("Learn Spring", "Description");

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.getFirst().getTitle()).isEqualTo("Learn Spring");
    }

    @Test
    public void shouldCreateAndFetchMultipleTasks() throws Exception {
        // leer
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        //Post Task 1
        UUID id = createTask("Learn Spring", "Description");

        // Post Task 2
        UUID id2 = createTask("Learning Spring", "Desc");

        // Get Liste
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title", hasItem("Learn Spring")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].title", hasItem("Learning Spring")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description", hasItem("Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].description", hasItem("Desc")));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Learn Spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));
    }

    @Test
    public void shouldReturnValidationErrorWhenRequestInvalid()  throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");
        request.setDescription("");

        // POST /task mit JSON
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].field", hasItem("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].field", hasItem("description")));

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(0);
    }

    @Test
    void shouldStartTask() throws Exception {
        UUID id = createTask("Learn Spring", "Description");

        // Start Task
        changeStatus("/tasks/" + id + "/start");

        // Verify state via GET
        assertThat(getStatus(id)).isEqualTo("IN_PROGRESS");
    }

    @Test
    void shouldCompleteTask() throws Exception {
        UUID id = createTask("Learn Spring", "Description");

        // Start Task
        changeStatus("/tasks/" + id + "/start");

        // Complete Task
        changeStatus("/tasks/" + id + "/complete");

        // Verify state via GET
        assertThat(getStatus(id)).isEqualTo("COMPLETED");
    }

    @Test
    void shouldReopenTask() throws Exception {
        UUID id = createTask("Learn Spring", "Description");

        // Start Task
        changeStatus("/tasks/" + id + "/start");

        // Complete Task
        changeStatus("/tasks/" + id + "/complete");

        // Reopen Task
        changeStatus("/tasks/" + id + "/reopen");

        // Verify state via GET
        assertThat(getStatus(id)).isEqualTo("IN_PROGRESS");
    }

    @Test
    void shouldRejectCompleteWhenNotInProgress() throws Exception {
        UUID id = createTask("Learn Spring", "Description");

        // Complete Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + id + "/complete"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Task kann nicht abgeschlossen werden."));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Task nicht gefunden:" + id));
    }

    @Test
    void shouldReturnDeleted() throws Exception {
        UUID random = UUID.randomUUID();
        UUID id = createTask("Learning Spring", "Desc");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/" + random))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Task nicht gefunden:" + random));


        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/" + id))
                .andExpect(status().isNotFound());
    }
}
