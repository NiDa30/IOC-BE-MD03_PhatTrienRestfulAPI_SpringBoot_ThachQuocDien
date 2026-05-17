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

    public List<Enrollment> findAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        if (enrollment.getCourseId() != null && courseRepository.findById(enrollment.getCourseId()) == null) {
            throw new IllegalArgumentException("Course with ID " + enrollment.getCourseId() + " does not exist.");
        }
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        if (enrollment.getCourseId() != null && courseRepository.findById(enrollment.getCourseId()) == null) {
            throw new IllegalArgumentException("Course with ID " + enrollment.getCourseId() + " does not exist.");
        }
        return enrollmentRepository.save(id, enrollment);
    }

    public Enrollment deleteEnrollmentById(Long id) {
        return enrollmentRepository.deleteById(id);
    }
}
