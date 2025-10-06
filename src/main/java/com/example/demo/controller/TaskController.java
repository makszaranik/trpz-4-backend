package com.example.demo.controller;


import com.example.demo.dto.task.TaskRequestDto;
import com.example.demo.dto.task.TaskDeletionRequestDto;
import com.example.demo.dto.task.TaskResponseDto;
import com.example.demo.dto.task.TaskSubmissionRequestDto;
import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.model.task.TaskEntity;
import com.example.demo.service.submission.SubmissionService;
import com.example.demo.service.task.TaskMapper;
import com.example.demo.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final SubmissionService submissionService;


    @PostMapping("submit")
    @ResponseStatus(HttpStatus.OK)
    public SseEmitter submitTask(@RequestBody @Valid TaskSubmissionRequestDto submitDto) {
        SubmissionEntity submission = submissionService.createSubmission(submitDto);
        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .id(submission.getId())
                        .name("event")
                        .data(submissionService.findSubmissionById(submission.getId()));

                emitter.send(event);
                Thread.sleep(5000);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return emitter;
    }


    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createTask(@RequestBody @Valid TaskRequestDto createDto) {
        TaskEntity task = taskMapper.toEntity(createDto, "ownerId");
        return taskService.save(task);
    }

    @DeleteMapping("delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@RequestBody @Valid TaskDeletionRequestDto deleteDto) {
        taskService.removeTaskEntity(deleteDto.taskId());
    }

    @PutMapping("update")
    @ResponseStatus(HttpStatus.OK)
    public void updateTask(@RequestBody @Valid TaskRequestDto updateDto) {
        taskService.updateTask(taskMapper.toEntity(updateDto, "ownerId"));
    }


    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto findTask(@PathVariable String id) {
        TaskEntity task = taskService.findTaskEntityById(id);
        return taskMapper.toResponseDto(task);
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> findAllTasks() {
        List<TaskEntity> tasks = taskService.findAll();
        return tasks.stream()
                .map(taskMapper::toResponseDto)
                .toList();
    }

}
