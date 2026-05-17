package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            SELECT new com.example.coursemanagementsystem.dto.StudentResponse(
                s.id,
                s.name
            )
            FROM Student s
            WHERE (:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<StudentResponse> searchStudents(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
