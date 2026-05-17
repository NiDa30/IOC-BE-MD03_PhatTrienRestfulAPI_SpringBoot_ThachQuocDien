package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {
    private final List<Student> students = new ArrayList<>();

    public StudentRepository() {
        students.add(new Student(1L, "Tran Van B", "b.tran@gmail.com"));
        students.add(new Student(2L, "Le Thi C", "c.le@gmail.com"));
    }

    public List<Student> findAll() {
        return students;
    }

    public Optional<Student> findById(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }

    public Student create(Student student) {
        if (student.getId() == null) {
            long newId = students.stream()
                    .mapToLong(Student::getId)
                    .max()
                    .orElse(0L) + 1;
            student.setId(newId);
        } else {
            Optional<Student> existingOpt = findById(student.getId());
            if (existingOpt.isPresent()) {
                Student existing = existingOpt.get();
                existing.setName(student.getName());
                existing.setEmail(student.getEmail());
                return existing;
            }
        }
        students.add(student);
        return student;
    }

    public Student update(Long id, Student updatedStudent) {
        Student existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        return existing;
    }

    public Student deleteById(Long id) {
        Student existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        students.remove(existing);
        return existing;
    }
}
