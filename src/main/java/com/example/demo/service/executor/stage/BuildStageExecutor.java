package com.example.demo.service.executor.stage;

import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.service.submission.SubmissionService;
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

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("build")
@RequiredArgsConstructor
public class BuildStageExecutor implements StageExecutor {

    private final DockerClient dockerClient;
    private final SubmissionService submissionService;

    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {

        log.info("Building stage {}.", submission.getId());
        String solutionUri = String.format("http://app:8080/files/download/%s", submission.getSourceCodeFileId());

        Integer statusCode = buildJob(submission, solutionUri);

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

    @SneakyThrows
    public Integer buildJob(SubmissionEntity submission, String... args) {
        Integer statusCode;
        String containerId = "";

        try {
            CreateContainerResponse container = dockerClient
                    .createContainerCmd("compile_stage")
                    .withCmd(args)
                    .withHostConfig(HostConfig.newHostConfig().withNetworkMode("demo_default"))
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .withTty(true)
                    .withName("compile-container")
                    .exec();

            containerId = container.getId();
            dockerClient.startContainerCmd(containerId).exec();

            log.info("container running {}.", containerId);

            statusCode = dockerClient
                    .waitContainerCmd(containerId)
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode(60, TimeUnit.SECONDS); //await build for 60 sec

            log.info("container {} finished.", containerId);

        } catch (Exception e) {
            statusCode = -1;
            log.error(e.getMessage(), e);

        } finally {

            dockerClient.removeContainerCmd(containerId)
                    .withRemoveVolumes(true)
                    .withForce(true)
                    .exec();
        }
        return statusCode;
    }
}
