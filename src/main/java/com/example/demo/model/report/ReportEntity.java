package com.example.demo.model.report;

import ch.qos.logback.core.status.Status;
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
@Document("reports")
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {

    @Id
    private String id;
    private Status status;
    private String submissionId;
    private String unitTestsResult;
    private String lintersResult;

    @CreatedDate
    private LocalDateTime createdAt;
}