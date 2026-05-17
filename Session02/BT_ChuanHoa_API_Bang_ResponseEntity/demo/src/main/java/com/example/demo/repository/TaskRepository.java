package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    public TaskRepository() {
        tasks.add(new Task(1L, "Fix login bug", "Fix the bug preventing user login", "High", "1"));
        tasks.add(new Task(2L, "Update DB schema", "Add new columns to user table", "Medium", "1"));
        tasks.add(new Task(3L, "Write unit tests", "Increase code coverage to 80%", "Low", "2"));
        tasks.add(new Task(4L, "Refactor code", "Refactor the payment module", "Medium", "2"));
        tasks.add(new Task(5L, "Design new UI", "Create mockups for dashboard", "High", "3"));
        tasks.add(new Task(6L, "Implement API", "Create REST endpoints for products", "High", "3"));
        tasks.add(new Task(7L, "Setup CI/CD", "Configure Jenkins pipeline", "High", "1"));
        tasks.add(new Task(8L, "Review PRs", "Review pending pull requests", "Medium", "2"));
        tasks.add(new Task(9L, "Update docs", "Update API documentation", "Low", "3"));
        tasks.add(new Task(10L, "Deploy to prod", "Deploy latest release to production", "High", "1"));
    }

    public List<Task> findAll() {
        return tasks;
    }

    public Task save(Task task) {
        long newId = tasks.stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0L) + 1;
        task.setId(newId);
        tasks.add(task);
        return task;
    }
}
