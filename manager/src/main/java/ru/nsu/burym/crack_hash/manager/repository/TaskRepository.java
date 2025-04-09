package ru.nsu.burym.crack_hash.manager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nsu.burym.crack_hash.manager.model.TaskInfo;

import java.util.UUID;

public interface TaskRepository extends MongoRepository<TaskInfo, UUID> {
}
