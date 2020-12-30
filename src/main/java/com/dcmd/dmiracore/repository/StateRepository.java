package com.dcmd.dmiracore.repository;

import com.dcmd.dmiracore.model.State;
import com.dcmd.dmiracore.model.enums.EState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StateRepository extends MongoRepository<State, String> {
    Optional<State> findByState(EState state);
}
