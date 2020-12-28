package com.dcmd.dmiracore.repository;

import com.dcmd.dmiracore.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Set<User> findUserByUsernameIn(Set<String> username);
}
