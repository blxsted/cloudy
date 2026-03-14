package com.cloudy.demo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTaskRequest {

    @NotNull
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    public CreateTaskRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }
}
