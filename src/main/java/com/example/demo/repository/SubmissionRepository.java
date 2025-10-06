package com.example.demo.repository;

import com.example.demo.model.submission.SubmissionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends MongoRepository<SubmissionEntity, String> {
    List<SubmissionEntity> findAllByStatus(SubmissionEntity.Status status);
    Optional<SubmissionEntity> findSubmissionEntityById(String id);
}