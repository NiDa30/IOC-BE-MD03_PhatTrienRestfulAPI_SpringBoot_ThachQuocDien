package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    public TaskRepository() {
        tasks.add(new Task(1L, "Fix login bug", "Fix the bug preventing user login", "High", "admin"));
        tasks.add(new Task(2L, "Update DB schema", "Add new columns to user table", "Medium", "admin"));
        tasks.add(new Task(3L, "Write unit tests", "Increase code coverage to 80%", "Low", "manager"));
        tasks.add(new Task(4L, "Refactor code", "Refactor the payment module", "Medium", "manager"));
        tasks.add(new Task(5L, "Design new UI", "Create mockups for dashboard", "High", "user"));
        tasks.add(new Task(6L, "Implement API", "Create REST endpoints for products", "High", "user"));
        tasks.add(new Task(7L, "Setup CI/CD", "Configure Jenkins pipeline", "High", "admin"));
        tasks.add(new Task(8L, "Review PRs", "Review pending pull requests", "Medium", "manager"));
        tasks.add(new Task(9L, "Update docs", "Update API documentation", "Low", "user"));
        tasks.add(new Task(10L, "Deploy to prod", "Deploy latest release to production", "High", "admin"));
    }

    public List<Task> findAll() {
        return tasks;
    }

    public Task findById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Task save(int id, Task updatedTask) {
        Task existingTask = findById(id);
        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setAssignedTo(updatedTask.getAssignedTo());
            return existingTask;
        }
        return null;
    }

    public Task deleteById(int id) {
        Task existingTask = findById(id);
        if (existingTask != null) {
            tasks.remove(existingTask);
            return existingTask;
        }
        return null;
    }
}
