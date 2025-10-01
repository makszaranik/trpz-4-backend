package com.example.demo.dto.task;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TaskCreationRequestDto(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String statement,
        @NotNull LocalTime timeRestriction,
        @Min(0) int memoryRestriction,
        @NotNull String solutionTemplateFileId,
        @NotNull String testsFileId,
        @NotNull String lintersFileId,
        @Min(0) @Max(100) int testsPoints,
        @Min(0) @Max(100) int lintersPoints,
        @Min(1) int submissionsNumberLimit
) {
}
