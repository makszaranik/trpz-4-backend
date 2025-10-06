package com.example.demo.service.executor.stage;

import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.service.submission.SubmissionService;
import com.example.demo.service.task.TaskService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("build")
public class BuildStageExecutor extends DockerStageExecutor {

    private final SubmissionService submissionService;

    @Autowired
    public BuildStageExecutor(DockerClient dockerClient, SubmissionService submissionService) {
        super(dockerClient, submissionService);
        this.submissionService = submissionService;
    }

    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {

        log.info("Build stage for submission {}.", submission.getId());
        String solutionUri = String.format("http://app:8080/files/download/%s", submission.getSourceCodeFileId());

        Integer statusCode = runJob(
                "compile_stage",
                "compile-container",
                submission,
                solutionUri
        );

        log.info("Status code is {}", statusCode);
        if (statusCode == 0) {
            submission.setStatus(SubmissionEntity.Status.COMPILATION_SUCCESS);
            submissionService.save(submission);
            chain.doNext(submission, chain);
        } else {
            submission.setStatus(SubmissionEntity.Status.COMPILATION_ERROR);
            submissionService.save(submission);
        }
    }
}


