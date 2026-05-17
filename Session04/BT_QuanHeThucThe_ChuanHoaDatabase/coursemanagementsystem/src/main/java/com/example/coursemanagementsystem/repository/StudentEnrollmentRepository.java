package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentEnrollmentRepository {
    private final List<StudentEnrollment> studentEnrollments = new ArrayList<>();

    public StudentEnrollmentRepository() {
        Instructor mockInstructor = new Instructor(1L, "Thach Quoc Dien", "dien.tq@gmail.com");
        
        Student s1 = new Student(1L, "Tran Van B", "b.tran@gmail.com");
        Course c1 = new Course(1L, "Intro Java", CourseStatus.ACTIVE, mockInstructor);
        studentEnrollments.add(new StudentEnrollment(1L, s1, c1));

        Student s2 = new Student(2L, "Le Thi C", "c.le@gmail.com");
        Course c2 = new Course(2L, "Spring Boot Advanced", CourseStatus.INACTIVE, mockInstructor);
        studentEnrollments.add(new StudentEnrollment(2L, s2, c2));
    }

    public List<StudentEnrollment> findAll() {
        return studentEnrollments;
    }

    public Optional<StudentEnrollment> findById(Long id) {
        return studentEnrollments.stream()
                .filter(se -> se.getId().equals(id))
                .findFirst();
    }

    public StudentEnrollment create(StudentEnrollment enrollment) {
        if (enrollment.getId() == null) {
            long newId = studentEnrollments.stream()
                    .mapToLong(StudentEnrollment::getId)
                    .max()
                    .orElse(0L) + 1;
            enrollment.setId(newId);
        } else {
            Optional<StudentEnrollment> existingOpt = findById(enrollment.getId());
            if (existingOpt.isPresent()) {
                StudentEnrollment existing = existingOpt.get();
                existing.setStudent(enrollment.getStudent());
                existing.setCourse(enrollment.getCourse());
                return existing;
            }
        }
        studentEnrollments.add(enrollment);
        return enrollment;
    }

    public StudentEnrollment update(Long id, StudentEnrollment updatedEnrollment) {
        StudentEnrollment existing = findById(id)
                .orElseThrow(() -> new RuntimeException("StudentEnrollment not found"));
        existing.setStudent(updatedEnrollment.getStudent());
        existing.setCourse(updatedEnrollment.getCourse());
        return existing;
    }

    public StudentEnrollment deleteById(Long id) {
        StudentEnrollment existing = findById(id)
                .orElseThrow(() -> new RuntimeException("StudentEnrollment not found"));
        studentEnrollments.remove(existing);
        return existing;
    }
}
