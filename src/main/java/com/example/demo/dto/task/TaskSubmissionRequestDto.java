package com.example.demo.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskSubmissionRequestDto(
        @NotNull @NotBlank String taskId,
        @NotNull @NotBlank String sourceCodeFileId
) {}
