package com.example.demo.repository;

import com.example.demo.model.submission.SubmissionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends MongoRepository<SubmissionEntity, String> {

    List<SubmissionEntity> findAllByUserId(String userId);
    List<SubmissionEntity> findAllByTaskId(String taskId);
    List<SubmissionEntity> findAllByStatus(SubmissionEntity.Status status);
}