package com.example.demo.service.executor.stage;

import com.example.demo.model.submission.SubmissionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("build")
@RequiredArgsConstructor
public class BuildStageExecutor implements StageExecutor {

    @Override
    public void execute(SubmissionEntity submission, StageExecutorChain chain) {
        log.info("Building stage {}.", submission.getId());
        chain.doNext(submission, chain);
    }
}
