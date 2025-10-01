package com.example.demo.repository;

import com.example.demo.model.report.ReportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends MongoRepository<ReportEntity, String> {

    Optional<ReportEntity> findBySubmissionId(String submissionId);

}