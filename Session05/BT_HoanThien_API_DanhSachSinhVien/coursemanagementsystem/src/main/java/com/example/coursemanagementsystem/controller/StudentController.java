package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createStudent(@RequestBody Student student) {
        studentService.createStudent(student);
        return ResponseEntity.ok(ApiResponse.success("Student Created Successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Sort.Direction direction,
            @RequestParam(required = false) String keyword
    ) {
        PageResponse<StudentResponse> list = studentService.searchStudents(page, size, sortBy, direction, keyword);
        return ResponseEntity.ok(ApiResponse.success("Ok", list));
    }
}
