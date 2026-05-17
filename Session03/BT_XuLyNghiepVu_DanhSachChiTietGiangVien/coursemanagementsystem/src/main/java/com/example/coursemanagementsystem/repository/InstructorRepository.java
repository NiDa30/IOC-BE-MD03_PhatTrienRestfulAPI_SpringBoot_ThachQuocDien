package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Instructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InstructorRepository {
    private final List<Instructor> instructors = new ArrayList<>();

    public InstructorRepository() {
        instructors.add(new Instructor(1L, "Thach Quoc Dien", "dien.tq@gmail.com"));
        instructors.add(new Instructor(2L, "Nguyen Van A", "a.nguyen@gmail.com"));
    }

    public List<Instructor> findAll() {
        return instructors;
    }

    public Optional<Instructor> findById(Long id) {
        return instructors.stream()
                .filter(instructor -> instructor.getId().equals(id))
                .findFirst();
    }

    public Instructor create(Instructor instructor) {
        if (instructor.getId() == null) {
            long newId = instructors.stream()
                    .mapToLong(Instructor::getId)
                    .max()
                    .orElse(0L) + 1;
            instructor.setId(newId);
        } else {
            Optional<Instructor> existingOpt = findById(instructor.getId());
            if (existingOpt.isPresent()) {
                Instructor existing = existingOpt.get();
                existing.setName(instructor.getName());
                existing.setEmail(instructor.getEmail());
                return existing;
            }
        }
        instructors.add(instructor);
        return instructor;
    }

    public Instructor update(Long id, Instructor updatedInstructor) {
        Instructor existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        existing.setName(updatedInstructor.getName());
        existing.setEmail(updatedInstructor.getEmail());
        return existing;
    }

    public Instructor deleteById(Long id) {
        Instructor existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        instructors.remove(existing);
        return existing;
    }
}
