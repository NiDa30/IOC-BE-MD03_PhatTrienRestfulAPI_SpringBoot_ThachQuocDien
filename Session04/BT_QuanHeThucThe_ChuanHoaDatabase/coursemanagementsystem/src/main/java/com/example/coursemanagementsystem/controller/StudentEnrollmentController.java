package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import com.example.coursemanagementsystem.service.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class StudentEnrollmentController {
    private final StudentEnrollmentService studentEnrollmentService;

    @Autowired
    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentEnrollment>>> getAllEnrollments() {
        List<StudentEnrollment> list = studentEnrollmentService.getAllEnrollment();
        return ResponseEntity.ok(ApiResponse.success("Fetch enrollments successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollment>> getEnrollmentById(@PathVariable Long id) {
        try {
            StudentEnrollment enrollment = studentEnrollmentService.getEnrollmentById(id);
            return ResponseEntity.ok(ApiResponse.success("Fetch enrollment successfully", enrollment));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentEnrollment>> createEnrollment(@RequestBody StudentEnrollment enrollment) {
        try {
            StudentEnrollment created = studentEnrollmentService.createEnrollment(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Enrollment created successfully", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollment>> updateEnrollment(@PathVariable Long id, @RequestBody StudentEnrollment enrollment) {
        try {
            StudentEnrollment updated = studentEnrollmentService.updateEnrollment(id, enrollment);
            return ResponseEntity.ok(ApiResponse.success("Enrollment updated successfully", updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollment>> deleteEnrollment(@PathVariable Long id) {
        try {
            StudentEnrollment deleted = studentEnrollmentService.deleteEnrollmentById(id);
            return ResponseEntity.ok(ApiResponse.success("Enrollment deleted successfully", deleted));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/enroll-course")
    public ResponseEntity<ApiResponse<EnrollmentDetail>> enrollCourse(@RequestBody EnrollCourseRequest request) {
        try {
            EnrollmentDetail detail = studentEnrollmentService.enrollCourse(request);
            return ResponseEntity.ok(ApiResponse.success("Enrollment successful", detail));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
