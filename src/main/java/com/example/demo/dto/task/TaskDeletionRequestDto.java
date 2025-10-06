package com.example.demo.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskDeletionResponseDto(
        @NotNull String taskId
)
{}