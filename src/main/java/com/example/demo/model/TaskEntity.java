package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@Document("tasks")
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    private String id;
    private String title;
    private String statement;
    private String ownerId;
    private LocalTime timeRestriction;
    private int memoryRestriction;
    private String solutionTemplateFileId;
    private String testsFileId;
    private String lintersFileId;
    private int testsPoints;
    private int lintersPoints;
    private int submissionsNumberLimit;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;



}
