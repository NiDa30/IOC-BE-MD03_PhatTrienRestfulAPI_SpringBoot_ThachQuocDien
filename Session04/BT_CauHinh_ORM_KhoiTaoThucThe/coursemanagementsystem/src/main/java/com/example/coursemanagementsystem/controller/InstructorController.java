package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.ApiResponse;
import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {
    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Instructor>>> getAllInstructors() {
        List<Instructor> list = instructorService.getAllInstructor();
        return ResponseEntity.ok(ApiResponse.success("Fetch instructors successfully", list));
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<List<InstructorDetail>>> getInstructorDetails() {
        List<InstructorDetail> details = instructorService.getInstructorDetails();
        return ResponseEntity.ok(ApiResponse.success("Fetch instructor details successfully", details));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> getInstructorById(@PathVariable Long id) {
        try {
            Instructor instructor = instructorService.getInstructorById(id);
            return ResponseEntity.ok(ApiResponse.success("Fetch instructor successfully", instructor));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Instructor>> createInstructor(@RequestBody Instructor instructor) {
        Instructor created = instructorService.createInstructor(instructor);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Instructor created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> updateInstructor(@PathVariable Long id, @RequestBody Instructor instructor) {
        try {
            Instructor updated = instructorService.updateInstructor(id, instructor);
            return ResponseEntity.ok(ApiResponse.success("Instructor updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Instructor>> deleteInstructor(@PathVariable Long id) {
        try {
            Instructor deleted = instructorService.deleteInstructorById(id);
            return ResponseEntity.ok(ApiResponse.success("Instructor deleted successfully", deleted));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
