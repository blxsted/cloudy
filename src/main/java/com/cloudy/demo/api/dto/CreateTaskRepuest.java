package com.cloudy.demo.api.dto;

public class CreateTaskRepuest {

    private String title;
    private String description;

    public CreateTaskRepuest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }
}
