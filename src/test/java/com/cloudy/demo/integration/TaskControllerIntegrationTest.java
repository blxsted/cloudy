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

    @Test
    @Transactional
    public void shouldCreateTaskViaHttp() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Spring");
        request.setDescription("Description");

        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Learn Spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));

        List<Task> tasks = taskRepository.findAll();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.getFirst().getTitle()).isEqualTo("Learn Spring");
    }

    @Test
    public void shouldHandleTaskListLifecycle() throws Exception {
        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // leer
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

        //Post Task 1
        CreateTaskRequest request1 = new CreateTaskRequest();
        request1.setTitle("Learn Spring");
        request1.setDescription("Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UUID uuid = UUID.fromString(objectMapper.readTree(json).get("id").asText());

        // Post Task 2
        CreateTaskRequest request2 = new CreateTaskRequest();
        request2.setTitle("Learning Spring");
        request2.setDescription("Desc");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request2))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

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
                        .get("/tasks/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(uuid.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Learn Spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));
    }

    @Test
    @Transactional
    public void shouldReturnValidationErrorWhenRequestInvalid()  throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");
        request.setDescription("");

        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // POST /task mit JSON
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].field", hasItem("title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldErrors[*].field", hasItem("description")));

        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(0);
    }

    @Test
    public void shouldReturn404WhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Task nicht gefunden"));
    }

    private String asJsonString(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldStartTask() throws Exception {
        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // Create Task
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Spring");
        request.setDescription("Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UUID uuid = UUID.fromString(objectMapper.readTree(json).get("id").asText());

        // Start Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/start"))
                .andExpect(status().isNoContent());

        // Verify state via GET
        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String getJson = getResult.getResponse().getContentAsString();
        String status = objectMapper.readTree(getJson).get("status").asText();

        assertThat(status).isEqualTo("IN_PROGRESS");
    }

    @Test
    void shouldCompleteTask() throws Exception {
        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // Create Task
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Spring");
        request.setDescription("Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UUID uuid = UUID.fromString(objectMapper.readTree(json).get("id").asText());

        // Start Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/start"))
                .andExpect(status().isNoContent());

        // Complete Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/complete"))
                .andExpect(status().isNoContent());

        // Verify state via GET
        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String getJson = getResult.getResponse().getContentAsString();
        String status = objectMapper.readTree(getJson).get("status").asText();

        assertThat(status).isEqualTo("COMPLETED");
    }

    @Test
    void shouldReopenTask() throws Exception {
        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // Create Task
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Spring");
        request.setDescription("Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UUID uuid = UUID.fromString(objectMapper.readTree(json).get("id").asText());

        // Start Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/start"))
                .andExpect(status().isNoContent());

        // Complete Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/complete"))
                .andExpect(status().isNoContent());

        // Reopen Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/reopen"))
                .andExpect(status().isNoContent());

        // Verify state via GET
        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/tasks/" + uuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String getJson = getResult.getResponse().getContentAsString();
        String status = objectMapper.readTree(getJson).get("status").asText();

        assertThat(status).isEqualTo("IN_PROGRESS");
    }

    @Test
    void shouldRejectCompleteWhenNotInProgress() throws Exception {
        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // Create Task
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Learn Spring");
        request.setDescription("Description");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        UUID uuid = UUID.fromString(objectMapper.readTree(json).get("id").asText());

        // Complete Task
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/tasks/" + uuid + "/complete"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Task kann nicht abgeschlossen werden."));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Task nicht gefunden"));
        ;
    }
}
