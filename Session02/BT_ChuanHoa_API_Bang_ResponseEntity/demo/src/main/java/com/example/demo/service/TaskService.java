package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task newTask) {
        if (newTask.getAssignedTo() == null) {
            return null;
        }

        try {
            int userId = Integer.parseInt(newTask.getAssignedTo());
            User user = userService.findUserById(userId);
            if (user != null) {
                return taskRepository.save(newTask);
            }
        } catch (NumberFormatException e) {
            // assignedTo is not a valid integer id
        }

        return null;
    }
}
