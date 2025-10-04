package com.example.demo.dto.task;

import lombok.Data;

@Data
public class TaskResponseDto {
    private String id;
    private String title;
    private String statement;
    private int timeRestriction;
    private int memoryRestriction;
    private int testsPoints;
    private int lintersPoints;
    private int submissionsNumberLimit;
    private String ownerId;
}
