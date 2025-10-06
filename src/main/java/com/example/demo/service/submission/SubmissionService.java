package com.example.demo.service.submission;


import com.example.demo.dto.task.TaskSubmissionRequestDto;
import com.example.demo.model.submission.SubmissionEntity;
import com.example.demo.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionEntity createSubmission(TaskSubmissionRequestDto submitDto) {
        SubmissionEntity taskSubmission = SubmissionEntity.builder()
                .taskId(submitDto.taskId())
                .userId("userId")
                .sourceCodeFileId(submitDto.sourceCodeFileId())
                .status(SubmissionEntity.Status.SUBMITTED)
                .logs("")
                .build();
        return submissionRepository.save(taskSubmission);
    }


    public List<SubmissionEntity> getAllSubmitted() {
        return submissionRepository.findAllByStatus(SubmissionEntity.Status.SUBMITTED);
    }

    public void save(SubmissionEntity submissionEntity) {
        submissionRepository.save(submissionEntity);
    }
}
