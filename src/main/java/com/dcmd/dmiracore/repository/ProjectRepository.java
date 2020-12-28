package com.dcmd.dmiracore.repository;

import com.dcmd.dmiracore.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProjectRepository extends MongoRepository<Project, String> {
    Boolean existsProjectsByName(String name);

    Boolean existsProjectsByTag(String tag);

    Optional<Project> findProjectByName(String name);
}
