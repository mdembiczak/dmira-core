package com.dcmd.dmiracore.repository;

import com.dcmd.dmiracore.model.ERole;
import com.dcmd.dmiracore.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findRoleByName(ERole name);
}
