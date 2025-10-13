package com.example.demo.controller;


import com.example.demo.dto.task.*;
import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.model.task.TaskEntity;
import com.example.demo.model.user.UserEntity.UserRole;
import com.example.demo.security.PreAuthorize;
import com.example.demo.security.UserContext;
import com.example.demo.service.submission.SubmissionService;
import com.example.demo.service.task.TaskMapper;
import com.example.demo.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final SubmissionService submissionService;
    private final UserContext context;

    @PostMapping(path = "submit")
    @PreAuthorize(roles = {UserRole.ADMIN, UserRole.STUDENT, UserRole.TEACHER})
    public SubmissionEntity submitTask(@RequestBody @Valid TaskSubmissionRequestDto submitDto) {
        return submissionService.createSubmission(submitDto);
    }


    @PostMapping(path = "status")
    @PreAuthorize(roles = {UserRole.ADMIN, UserRole.STUDENT, UserRole.TEACHER})
    public SubmissionEntity taskStatus(@RequestBody @Valid TaskSubmissionStatusRequestDto requestDto) {
        return submissionService.findSubmissionById(requestDto.submissionId());
    }


    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(roles = {UserRole.ADMIN, UserRole.TEACHER})
    public String createTask(@RequestBody @Valid TaskRequestDto createDto) {
        String ownerId = context.get().getId();
        TaskEntity task = taskMapper.toEntity(createDto, ownerId);
        return taskService.save(task);
    }


    @DeleteMapping("delete")
    @PreAuthorize(roles = {UserRole.ADMIN, UserRole.TEACHER})
    public void deleteTask(@RequestBody @Valid TaskDeletionRequestDto deleteDto) {
        taskService.removeTaskEntity(deleteDto.taskId());
    }


    @PutMapping("update")
    @PreAuthorize(roles = {UserRole.ADMIN, UserRole.TEACHER})
    public void updateTask(@RequestBody @Valid TaskRequestDto updateDto) {
        String ownerId = context.get().getId();
        taskService.updateTask(taskMapper.toEntity(updateDto, ownerId));
    }


    @GetMapping("{id}")
    public TaskResponseDto findTask(@PathVariable String id) {
        TaskEntity task = taskService.findTaskById(id);
        return taskMapper.toResponseDto(task);
    }


    @GetMapping
    public List<TaskResponseDto> findAllTasks() {
        List<TaskEntity> tasks = taskService.findAll();
        return tasks.stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

}
