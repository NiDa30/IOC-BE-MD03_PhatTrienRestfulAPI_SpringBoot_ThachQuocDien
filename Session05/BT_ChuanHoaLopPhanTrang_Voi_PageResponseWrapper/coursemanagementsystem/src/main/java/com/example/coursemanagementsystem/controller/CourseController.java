package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponse>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        PageResponse<CourseResponse> list = courseService.getPagedCourses(page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success("Ok", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id) {
        try {
            CourseResponse course = courseService.getCourseById(id);
            return ResponseEntity.ok(ApiResponse.success("Fetch course successfully", course));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody CourseCreateRequest request) {
        try {
            CourseResponse created = courseService.createCourse(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Course Created Successfully", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateRequest request) {
        try {
            CourseResponse updated = courseService.updateCourse(id, request);
            return ResponseEntity.ok(ApiResponse.success("Course Updated Successfully", updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> deleteCourse(@PathVariable Long id) {
        try {
            CourseResponse deleted = courseService.deleteCourseById(id);
            return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", deleted));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
