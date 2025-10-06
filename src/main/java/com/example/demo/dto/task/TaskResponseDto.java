package com.example.demo.dto.task;


public record TaskResponseDto(
        String id,
        String title,
        String statement,
        int timeRestriction,
        int memoryRestriction,
        int testsPoints,
        int lintersPoints,
        int submissionsNumberLimit,
        String ownerId
) {}
