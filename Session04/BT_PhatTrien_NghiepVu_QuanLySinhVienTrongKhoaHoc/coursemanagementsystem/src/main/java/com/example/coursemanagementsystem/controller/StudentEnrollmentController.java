package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.dto.StudentEnrollmentRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentRequest;
import com.example.coursemanagementsystem.dto.CourseEnrollmentResponse;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import com.example.coursemanagementsystem.service.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentEnrollmentController {
    private final StudentEnrollmentService studentEnrollmentService;

    @Autowired
    public StudentEnrollmentController(StudentEnrollmentService studentEnrollmentService) {
        this.studentEnrollmentService = studentEnrollmentService;
    }

    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse<List<StudentEnrollment>>> getAllEnrollments() {
        List<StudentEnrollment> list = studentEnrollmentService.getAllEnrollment();
        return ResponseEntity.ok(ApiResponse.success("Fetch enrollments successfully", list));
    }

    @GetMapping("/enrollments/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollment>> getEnrollmentById(@PathVariable Long id) {
        try {
            StudentEnrollment enrollment = studentEnrollmentService.getEnrollmentById(id);
            return ResponseEntity.ok(ApiResponse.success("Fetch enrollment successfully", enrollment));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/enrollments")
    public ResponseEntity<ApiResponse<StudentEnrollment>> createEnrollment(@RequestBody StudentEnrollment enrollment) {
        try {
            StudentEnrollment created = studentEnrollmentService.createEnrollment(enrollment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Enrollment created successfully", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/enrollments/{id}")
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

    @DeleteMapping("/enrollments/{id}")
    public ResponseEntity<ApiResponse<StudentEnrollment>> deleteEnrollment(@PathVariable Long id) {
        try {
            StudentEnrollment deleted = studentEnrollmentService.deleteEnrollmentById(id);
            return ResponseEntity.ok(ApiResponse.success("Enrollment deleted successfully", deleted));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/enrollments/enroll-course")
    public ResponseEntity<ApiResponse<EnrollmentDetail>> enrollCourse(@RequestBody EnrollCourseRequest request) {
        try {
            EnrollmentDetail detail = studentEnrollmentService.enrollCourse(request);
            return ResponseEntity.ok(ApiResponse.success("Enrollment successful", detail));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping({"/students/enrollments", "/students-enrollments"})
    public ResponseEntity<ApiResponse<Void>> enrollStudent(@RequestBody StudentEnrollmentRequest request) {
        try {
            studentEnrollmentService.enrollStudent(request.getStudentId(), request.getCourseId());
            return ResponseEntity.ok(ApiResponse.success("Student Enrolled Successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // New API Endpoints for BT_PhatTrien_NghiepVu_QuanLySinhVienTrongKhoaHoc

    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<ApiResponse<CourseEnrollmentResponse>> enrollStudentInCourse(
            @PathVariable Long courseId,
            @RequestBody CourseEnrollmentRequest request
    ) {
        try {
            CourseEnrollmentResponse response = studentEnrollmentService.enrollStudentInCourse(courseId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Student Enrolled Successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping({"/courses/{courseId}/enrollments/students/{studentId}", "/courses/{courseId}/students/{studentId}"})
    public ResponseEntity<Void> dropoutStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ) {
        try {
            studentEnrollmentService.dropoutStudentFromCourse(courseId, studentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/courses/{courseId}/enrollments/students")
    public ResponseEntity<ApiResponse<List<CourseEnrollmentResponse>>> searchStudents(
            @PathVariable Long courseId,
            @RequestParam("search") String search
    ) {
        try {
            List<CourseEnrollmentResponse> list = studentEnrollmentService.searchStudentsInCourse(courseId, search);
            return ResponseEntity.ok(ApiResponse.success("Search students successfully", list));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
