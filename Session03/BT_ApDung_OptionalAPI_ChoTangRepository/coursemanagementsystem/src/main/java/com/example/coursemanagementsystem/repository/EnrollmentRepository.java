package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EnrollmentRepository {
    private final List<Enrollment> enrollments = new ArrayList<>();

    public EnrollmentRepository() {
        enrollments.add(new Enrollment(1L, "Tran Van B", 1L));
        enrollments.add(new Enrollment(2L, "Le Thi C", 2L));
    }

    public List<Enrollment> findAll() {
        return enrollments;
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getId().equals(id))
                .findFirst();
    }

    public Enrollment create(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            long newId = enrollments.stream()
                    .mapToLong(Enrollment::getId)
                    .max()
                    .orElse(0L) + 1;
            enrollment.setId(newId);
        } else {
            Optional<Enrollment> existingOpt = findById(enrollment.getId());
            if (existingOpt.isPresent()) {
                Enrollment existing = existingOpt.get();
                existing.setStudentName(enrollment.getStudentName());
                existing.setCourseId(enrollment.getCourseId());
                return existing;
            }
        }
        enrollments.add(enrollment);
        return enrollment;
    }

    public Enrollment update(Long id, Enrollment updatedEnrollment) {
        Enrollment existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        existing.setStudentName(updatedEnrollment.getStudentName());
        existing.setCourseId(updatedEnrollment.getCourseId());
        return existing;
    }

    public Enrollment deleteById(Long id) {
        Enrollment existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollments.remove(existing);
        return existing;
    }
}
