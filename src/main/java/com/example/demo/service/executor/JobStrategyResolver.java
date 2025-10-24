package com.example.demo.service.executor;


import com.example.demo.service.executor.stage.JobStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JobStrategiesFactory {

    private final Map<String, JobStrategy> jobStrategies; // test -> TestJobStrategy...

    public JobStrategy getJobStrategy(String jobName) {
        return jobStrategies.get(jobName);
    }

    public enum JobStrategy {
        BUILD,
        TEST
    }
}
