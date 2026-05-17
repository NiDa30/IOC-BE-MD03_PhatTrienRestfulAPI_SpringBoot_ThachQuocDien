package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Instructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public Instructor findById(Long id) {
        return instructors.stream()
                .filter(instructor -> instructor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Instructor save(Instructor instructor) {
        if (instructor.getId() == null) {
            long newId = instructors.stream()
                    .mapToLong(Instructor::getId)
                    .max()
                    .orElse(0L) + 1;
            instructor.setId(newId);
        } else {
            // If ID is provided, check if it already exists to avoid duplicates
            Instructor existing = findById(instructor.getId());
            if (existing != null) {
                existing.setName(instructor.getName());
                existing.setEmail(instructor.getEmail());
                return existing;
            }
        }
        instructors.add(instructor);
        return instructor;
    }

    public Instructor save(Long id, Instructor updatedInstructor) {
        Instructor existing = findById(id);
        if (existing != null) {
            existing.setName(updatedInstructor.getName());
            existing.setEmail(updatedInstructor.getEmail());
            return existing;
        }
        return null;
    }

    public Instructor deleteById(Long id) {
        Instructor existing = findById(id);
        if (existing != null) {
            instructors.remove(existing);
            return existing;
        }
        return null;
    }
}
