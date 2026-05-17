package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollment, Long> {
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    Optional<StudentEnrollment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    @Query("SELECT se FROM StudentEnrollment se WHERE se.course.id = :courseId AND LOWER(se.student.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<StudentEnrollment> searchStudentsInCourse(Long courseId, String search);
}
