package com.dcmd.dmiracore.repository;

import com.dcmd.dmiracore.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends MongoRepository<Task, String> {
    Optional<Task> findTasksByName(String name);

    Set<Task> findTasksByNameIn(Set<String> tasks);

    Boolean existsByName(String name);


}
