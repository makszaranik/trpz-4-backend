package com.example.demo.controller;


import com.example.demo.dto.task.TaskCreationRequestDto;
import com.example.demo.dto.task.TaskSubmissionRequestDto;
import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.model.task.TaskEntity;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.task.TaskMapper;
import com.example.demo.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping("submit")
    public String submitTask(@RequestBody @Valid TaskSubmissionRequestDto submitDto) {

        SubmissionEntity taskSubmission = SubmissionEntity.builder()
                .taskId(submitDto.taskId())
                //.userId()
                .sourceCodeFileId(submitDto.sourceCodeFileId())
                .build();

        //RabbitMQ

    }


    @PostMapping("create")
    public String createTask(@RequestBody @Valid TaskCreationRequestDto createDto) {
        TaskEntity task = TaskMapper.toEntity(createDto, "ownerId");
        return taskService.save(task);
    }
}
