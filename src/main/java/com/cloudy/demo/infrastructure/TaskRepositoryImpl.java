package com.cloudy.demo.infrastructure;

import com.cloudy.demo.domain.Task;
import com.cloudy.demo.domain.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final SpringDataTaskRepository jpaRepository;
    private final TaskEntityMapper taskMapper;

    public TaskRepositoryImpl(SpringDataTaskRepository jpaRepository , TaskEntityMapper taskMapper) {
        this.jpaRepository = jpaRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(taskMapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(taskMapper::toDomain)
                .toList();
    }

    @Override
   public Task save(Task task){
        TaskJpaEntity entity = taskMapper.toJpaEntity(task);
        TaskJpaEntity savedEntity = jpaRepository.save(entity);
        return taskMapper.toDomain(savedEntity);
   }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

}
