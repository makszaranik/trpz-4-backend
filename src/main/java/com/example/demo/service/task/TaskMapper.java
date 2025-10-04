package com.example.demo.service.task;

import com.example.demo.dto.task.TaskCreationRequestDto;
import com.example.demo.dto.task.TaskResponseDto;
import com.example.demo.model.task.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(TaskCreationRequestDto createDto, String ownerId) {
        return TaskEntity.builder()
                .title(createDto.title())
                .statement(createDto.statement())
                .timeRestriction(createDto.timeRestriction())
                .memoryRestriction(createDto.memoryRestriction())
                .solutionTemplateFileId(createDto.solutionTemplateFileId())
                .testsFileId(createDto.testsFileId())
                .lintersFileId(createDto.lintersFileId())
                .testsPoints(createDto.testsPoints())
                .lintersPoints(createDto.lintersPoints())
                .ownerId(ownerId)
                .build();
    }

    public TaskResponseDto toResponseDto(TaskEntity entity) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setStatement(entity.getStatement());
        dto.setTimeRestriction(entity.getTimeRestriction());
        dto.setMemoryRestriction(entity.getMemoryRestriction());
        dto.setTestsPoints(entity.getTestsPoints());
        dto.setLintersPoints(entity.getLintersPoints());
        dto.setSubmissionsNumberLimit(entity.getSubmissionsNumberLimit());
        dto.setOwnerId(entity.getOwnerId());
        return dto;
    }
}
