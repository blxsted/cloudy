# Cloudy Task Management API

A cloud-ready backend for task management built with Spring Boot, PostgreSQL, and Docker, CI/CD and DevOps elements following Clean Architecture principles.

This project demonstrates separation between domain, application logic, and infrastructure layers.

---

## Features

* Create, retrieve, update and delete tasks
* Task lifecycle management (start, complete, reopen)
* Clean Architecture (domain-driven structure)
* Centralized exception handling
* Structured logging (controller + use case level)
* Dockerized application (backend + database)
* OpenAPI / Swagger documentation

---

## Architecture

The project follows a layered Clean Architecture approach:

* **domain** → core business logic and entities
* **application** → use cases (business workflows)
* **infrastructure** → database and external systems
* **api** → REST controllers and DTOs

This ensures clear separation of concerns and maintainability.

---

## Tech Stack:
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Docker & Docker Compose
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


## Running the Application

### Option 1 – Full Docker Setup (recommended)

```bash
docker-compose up --build
```

Application:
http://localhost:8080

---

### Option 2 – Local Backend + Docker Database

Start database:

```bash
./start-postgres.sh
```

Run backend:

```bash
mvn spring-boot:run
```

---

## API Documentation

Swagger UI:
http://localhost:8080/swagger-ui.html

OpenAPI JSON:
http://localhost:8080/v3/api-docs

---

## API Endpoints

### Create Task

POST /tasks


```json
{
    "title": "Learn Spring",
    "description": "Build a clean architecture project" 
}
```
Content-Type: application/json

### Get Task
GET /tasks/{id}

### Start Task

PATCH /tasks/{id}/start

---

### Complete Task

PATCH /tasks/{id}/complete

---

### Delete Task

DELETE /tasks/{id}

---

## Configuration

Environment variables (via `.env` or docker-compose):

* `SPRING_DATASOURCE_URL`
* `SPRING_DATASOURCE_USERNAME`
* `SPRING_DATASOURCE_PASSWORD`

---

## Learning Goals

This project focuses on:

* Designing maintainable backend systems
* Applying Clean Architecture in practice
* Understanding containerized environments
* Building production-like APIs with proper logging and error handling

---

## Notes

This project is part of a structured backend engineering learning roadmap.

More details about the architecture can be found in:
[Architecture](docs/architecture.md)
