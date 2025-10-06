package com.example.demo.service.executor.stage;


import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.model.task.TaskEntity;
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
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("test")
@RequiredArgsConstructor
public class TestStageExecutor implements StageExecutor {

    private final DockerClient dockerClient;
    private final SubmissionService submissionService;
    private final TaskService taskService;


    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {

        log.info("Test stage started {}.", submission.getId());
        TaskEntity task = taskService.findTaskEntityById(submission.getTaskId());
        String testsFileId = task.getTestsFileId();
        Integer timeRestriction = task.getTimeRestriction();

        String solutionUri = String.format("http://app:8080/files/download/%s", submission.getSourceCodeFileId());
        String testUri = String.format("http://app:8080/files/download/%s", testsFileId);

        Integer statusCode = testJob(timeRestriction, submission, solutionUri, testUri);

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

    @SneakyThrows
    public Integer testJob(Integer timeRestriction, SubmissionEntity submission, String... args) {
        Integer statusCode;
        String containerId = "";

        try {
            CreateContainerResponse container = dockerClient.createContainerCmd("test_stage")
                    .withCmd(args)
                    .withHostConfig(HostConfig.newHostConfig().withNetworkMode("demo_default"))
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withTty(true)
                    .withName("test-container")
                    .exec();

            containerId = container.getId();
            dockerClient.startContainerCmd(containerId).exec();

            log.info("container running {}.", containerId);

            statusCode = dockerClient.waitContainerCmd(containerId)
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode(timeRestriction, TimeUnit.SECONDS);

            log.info("container {} finished.", containerId);


        } catch (Exception e) {
            statusCode = -1;
            log.error("Test stage {} failed.", containerId, e);

        } finally {

            dockerClient.removeContainerCmd(containerId)
                    .withRemoveVolumes(true)
                    .withForce(true)
                    .exec();
        }
        return statusCode;
    }

}
