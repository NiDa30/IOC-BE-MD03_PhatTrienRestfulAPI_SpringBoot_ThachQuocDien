package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        users.add(new User(1L, "admin", "admin@example.com", "ADMIN"));
        users.add(new User(2L, "manager", "manager@example.com", "MANAGER"));
        users.add(new User(3L, "user", "user@example.com", "USER"));
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public User save(User user) {
        long newId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
        user.setId(newId);
        users.add(user);
        return user;
    }

    public User save(int id, User updatedUser) {
        User existingUser = findById(id);
        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());
            return existingUser;
        }
        return null;
    }

    public User deleteById(int id) {
        User existingUser = findById(id);
        if (existingUser != null) {
            users.remove(existingUser);
            return existingUser;
        }
        return null;
    }
}
