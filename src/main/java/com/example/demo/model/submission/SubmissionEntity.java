package com.example.demo.model.submission;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document("submissions")
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionEntity {

    @Id
    private String id;
    private String taskId;
    private String userId;
    private String sourceCodeFileId; //user uploaded sourceCodeId
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum Status {
        SUBMITTED,
        COMPILING,
        COMPILATION_SUCCESS,
        COMPILATION_ERROR,
        TESTING,
        TEST_COMPLETED_SUCCESS,
        TEST_FAILED,
        RUNTIME_ERROR,
        TIME_LIMIT_EXCEEDED,
        OUT_OF_MEMORY,
    }
}
