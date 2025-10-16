package com.example.demo.service.executor.stage;

import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.service.submission.SubmissionService;
import com.github.dockerjava.api.DockerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("build")
public class BuildStageExecutor extends DockerJobRunner implements StageExecutor {

    private final SubmissionService submissionService;

    @Autowired
    public BuildStageExecutor(DockerClient dockerClient, SubmissionService submissionService) {
        super(dockerClient);
        this.submissionService = submissionService;
    }


    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {

        log.info("Build stage for submission {}.", submission.getId());
        String downloadPath = "http://host.docker.internal:8080/files/download/%s";
        String solutionUri = String.format(downloadPath, submission.getSourceCodeFileId());

        String cmd = String.format(
                "wget -O solution.zip %s && unzip solution.zip" +
                        " && cd solution" +
                        " && mvn clean compile -q",
                solutionUri
        );

        JobResult jobResult = runJob(
                "build_container",
                submission,
                "/bin/bash", "-c", cmd
        );

        Integer statusCode = jobResult.statusCode();
        String logs = jobResult.logs();

        log.info("Status code is {}", statusCode);
        if (statusCode == 0) {
            submission.setStatus(SubmissionEntity.Status.COMPILATION_SUCCESS);
            submission.setLogs(logs);
            submissionService.save(submission);
            chain.doNext(submission, chain);
        } else {
            submission.setStatus(SubmissionEntity.Status.COMPILATION_ERROR);
            submission.setLogs(logs);
            submissionService.save(submission);
        }
    }
}


