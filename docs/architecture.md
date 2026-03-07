# Architecture

## Architectural Style

This project follows a simplified Clean Architecture / Hexagonal Architecture.

The goal is to separate business logic from framework and infrastructure concerns.

## Layer Overview

**Domain**  
Contains the core business model and repository interfaces.

**Application**  
Contains use cases that orchestrate domain logic.

**Infrastructure**  
Contains adapters such as persistence and web controllers.

## Dependency Rule

Dependencies only point inward:

Web → Application → Domain

Infrastructure implements interfaces defined in the domain.

## Domain Model

The core entity is Task.

A Task has:
- id
- title
- description
- createdAt
- status

The domain model is independent from JPA and Spring.

## Persistence

Persistence is implemented using Spring Data JPA.

The following components exist:

TaskJpaEntity – database representation  
TaskMapper – converts between domain and persistence model  
TaskRepositoryImpl – adapter implementing the domain repository

This allows the domain layer to remain independent from the persistence framework.

## API Layer

The API layer exposes REST endpoints.

DTOs are used to prevent exposing domain objects directly.

Endpoints:

POST /tasks  
GET /tasks/{id}

## Architecture Overview

                HTTP
                 │
                 ▼
        ┌──────────────────┐
        │   TaskController  │
        └──────────────────┘
                 │
                 ▼
        ┌──────────────────┐
        │     UseCases      │
        │ CreateTaskUseCase │
        │ GetTaskUseCase    │
        └──────────────────┘
                 │
                 ▼
        ┌──────────────────┐
        │   TaskRepository  │
        │     (Interface)   │
        └──────────────────┘
                 ▲
                 │
        ┌──────────────────────────┐
        │   TaskRepositoryImpl      │
        │   (Persistence Adapter)   │
        └──────────────────────────┘
                 │
                 ▼
        ┌──────────────────┐
        │ SpringData JPA    │
        │ TaskJpaEntity     │
        └──────────────────┘
                 │
                 ▼
               Database