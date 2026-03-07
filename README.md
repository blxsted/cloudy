# Cloudy Task Management API

Cloud ready Task-Manager with Spring, Docker, CI/CD and DevOps Elements using a clean architecture approach.

This learning project demonstrates separation between domain, application logic, and infrastructure layers.

## Tech Stack:
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- AWS

## Features
- Create a task
- Retrieve a task by ID
- Clean architecture structure
- DTO-based API layer

## Project Structure
domain – business model and repository interfaces  
application – use cases  
infrastructure – persistence and web adapters

## Running the Application
1. Clone the repository
2. Run `mvn sprint-boot:run`

The server starts on:
`http://Localhost:8080`


## API Endpoints

### Create Task

POST /tasks

Request:
```
{
    "title": "Learn Spring",
    "description": "Build a clean architecture project" 
}
```
Content-Type: application/json

### Get Task
GET /tasks/{id}

More details about the architecture can be found in:
[Architecture](docs/architecture.md)