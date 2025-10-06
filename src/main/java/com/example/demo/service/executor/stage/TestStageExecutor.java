package com.example.demo.service.executor.stage;

import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.model.task.TaskEntity;
import com.example.demo.service.submission.SubmissionService;
import com.example.demo.service.task.TaskService;
import com.github.dockerjava.api.DockerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component("test")
public class TestStageExecutor extends DockerStageExecutor {

    private final TaskService taskService;
    private final SubmissionService submissionService;

    @Autowired
    public TestStageExecutor(DockerClient dockerClient,
                             SubmissionService submissionService,
                             TaskService taskService, SubmissionService submissionService1
    ) {
        super(dockerClient, submissionService);
        this.taskService = taskService;
        this.submissionService = submissionService;
    }

    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {

        log.info("Test stage for submission {}.", submission.getId());
        TaskEntity task = taskService.findTaskEntityById(submission.getTaskId());
        String testsFileId = task.getTestsFileId();

        String solutionUri = String.format("http://app:8080/files/download/%s", submission.getSourceCodeFileId());
        String testUri = String.format("http://app:8080/files/download/%s", testsFileId);

        Integer statusCode = runJob(
                "test_stage",
                "test-container",
                submission,
                solutionUri,
                testUri
        );

        log.info("Status code is {}", statusCode);
        if(statusCode == 0){
            submission.setStatus(SubmissionEntity.Status.ACCEPTED);
            submissionService.save(submission);
            chain.doNext(submission, chain);
        } else {
            submission.setStatus(SubmissionEntity.Status.WRONG_ANSWER);
            submissionService.save(submission);
        }
    }

}
