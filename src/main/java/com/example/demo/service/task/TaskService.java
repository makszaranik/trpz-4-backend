package com.example.demo.service.task;

import com.example.demo.model.task.TaskEntity;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public String save(TaskEntity task) {
        return taskRepository.save(task).getId();
    }

    public List<TaskEntity> findAll(){
        return taskRepository.findAll();
    }
}
