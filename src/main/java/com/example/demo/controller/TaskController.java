package com.example.demo.controller;


import com.example.demo.dto.task.TaskCreationRequestDto;
import com.example.demo.dto.task.TaskResponseDto;
import com.example.demo.dto.task.TaskSubmissionRequestDto;
import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.model.task.TaskEntity;
import com.example.demo.service.submission.SubmissionService;
import com.example.demo.service.task.TaskMapper;
import com.example.demo.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final SubmissionService submissionService;

    @PostMapping("submit")
    public SubmissionEntity submitTask(@RequestBody @Valid TaskSubmissionRequestDto submitDto) {
        return submissionService.createSubmission(submitDto);
    }


    @PostMapping("create")
    public String createTask(@RequestBody @Valid TaskCreationRequestDto createDto) {
        TaskEntity task = taskMapper.toEntity(createDto, "ownerId");
        return taskService.save(task);
    }

    @GetMapping
    public List<TaskResponseDto> findAllTasks() {
        List<TaskEntity> tasks = taskService.findAll();
        return tasks.stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

}
