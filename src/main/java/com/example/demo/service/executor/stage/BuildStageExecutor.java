package com.example.demo.service.executor.stage;

import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.service.submission.SubmissionService;
import com.github.dockerjava.api.DockerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("build")
public class BuildStageExecutor extends DockerJobRunner implements StageExecutor {

    private final SubmissionService submissionService;

    @Autowired
    public BuildStageExecutor(DockerClient dockerClient, SubmissionService submissionService) {
        super(dockerClient, submissionService);
        this.submissionService = submissionService;
    }


    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {

        log.info("Build stage for submission {}.", submission.getId());
        String downloadPath = "http://app:8080/files/download/%s";
        String solutionUri = String.format(downloadPath, submission.getSourceCodeFileId());

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


