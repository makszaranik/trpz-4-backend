package com.example.demo.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskDeletionRequestDto(
        @NotNull String taskId
)
{}