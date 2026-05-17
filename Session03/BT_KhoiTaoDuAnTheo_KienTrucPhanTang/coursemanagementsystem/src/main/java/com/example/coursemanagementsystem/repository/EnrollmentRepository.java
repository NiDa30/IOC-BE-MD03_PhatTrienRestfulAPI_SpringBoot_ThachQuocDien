package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Enrollment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public Enrollment findById(Long id) {
        return enrollments.stream()
                .filter(enrollment -> enrollment.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            long newId = enrollments.stream()
                    .mapToLong(Enrollment::getId)
                    .max()
                    .orElse(0L) + 1;
            enrollment.setId(newId);
        } else {
            Enrollment existing = findById(enrollment.getId());
            if (existing != null) {
                existing.setStudentName(enrollment.getStudentName());
                existing.setCourseId(enrollment.getCourseId());
                return existing;
            }
        }
        enrollments.add(enrollment);
        return enrollment;
    }

    public Enrollment save(Long id, Enrollment updatedEnrollment) {
        Enrollment existing = findById(id);
        if (existing != null) {
            existing.setStudentName(updatedEnrollment.getStudentName());
            existing.setCourseId(updatedEnrollment.getCourseId());
            return existing;
        }
        return null;
    }

    public Enrollment deleteById(Long id) {
        Enrollment existing = findById(id);
        if (existing != null) {
            enrollments.remove(existing);
            return existing;
        }
        return null;
    }
}
