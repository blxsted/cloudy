package com.cloudy.demo.integration;

import com.cloudy.demo.api.dto.CreateTaskRepuest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    @Transactional
    public void shouldCreateTaskViaHttp() throws Exception {
        // request Body
        CreateTaskRepuest request = new CreateTaskRepuest();
        request.setTitle("Learn Spring");
        request.setDescription("Description");

        // Given Application läuft
        // und DB Container läuft
        System.out.println("DB URL: " + postgresContainer.getJdbcUrl());

        // When Eine Task über den Controller erstellt wird
        // POST /task mit JSON
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tasks")
                        .contentType("application/json")
                        .content(asJsonString(request))
                        .accept("application/json"))
                .andExpect(status().isOk()) // 201
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Learn Spring"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
