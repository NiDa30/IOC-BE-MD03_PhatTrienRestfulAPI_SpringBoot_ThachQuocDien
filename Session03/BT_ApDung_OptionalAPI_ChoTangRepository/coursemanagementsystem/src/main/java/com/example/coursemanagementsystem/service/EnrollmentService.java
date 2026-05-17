package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.model.Enrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
    }

    public List<Enrollment> getAllEnrollment() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        if (enrollment.getCourseId() != null) {
            courseRepository.findById(enrollment.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course with ID " + enrollment.getCourseId() + " does not exist."));
        }
        return enrollmentRepository.create(enrollment);
    }

    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        if (enrollment.getCourseId() != null) {
            courseRepository.findById(enrollment.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course with ID " + enrollment.getCourseId() + " does not exist."));
        }
        return enrollmentRepository.update(id, enrollment);
    }

    public Enrollment deleteEnrollmentById(Long id) {
        return enrollmentRepository.deleteById(id);
    }
}
