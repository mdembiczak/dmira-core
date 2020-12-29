package com.dcmd.dmiracore.repository;

import com.dcmd.dmiracore.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
