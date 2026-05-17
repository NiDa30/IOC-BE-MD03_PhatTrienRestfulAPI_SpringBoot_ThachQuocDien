package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.CourseCreateRequest;
import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseInstructorResponse;
import com.example.coursemanagementsystem.dto.CourseUpdateRequest;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    private CourseResponse mapToCourseResponse(Course course) {
        if (course == null) {
            return null;
        }
        CourseInstructorResponse instructorResponse = null;
        if (course.getInstructor() != null) {
            instructorResponse = new CourseInstructorResponse(
                course.getInstructor().getId(),
                course.getInstructor().getName()
            );
        }
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getStatus(),
            instructorResponse
        );
    }

    public List<CourseResponse> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(this::mapToCourseResponse)
                .toList();
    }

    public Page<CourseResponse> getPagedCourses(int page, int size, String sortBy, Sort.Direction direction) {
        if (page < 0) {
            page = 0;
        }
        String actualSortBy = (sortBy == null || sortBy.trim().isEmpty()) ? "id" : sortBy;
        Sort sort = Sort.by(direction, actualSortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return courseRepository.findAll(pageable).map(this::mapToCourseResponse);
    }

    public CourseResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(this::mapToCourseResponse)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public CourseResponse createCourse(CourseCreateRequest req) {
        Instructor instructor = instructorRepository.findById(req.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor with ID " + req.getInstructorId() + " does not exist."));

        Course course = new Course();
        course.setTitle(req.getTitle());
        course.setStatus(req.getStatus());
        course.setInstructor(instructor);
        return mapToCourseResponse(courseRepository.save(course));
    }

    public CourseResponse updateCourse(Long id, CourseUpdateRequest req) {
        Instructor instructor = instructorRepository.findById(req.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor with ID " + req.getInstructorId() + " does not exist."));

        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        existing.setTitle(req.getTitle());
        existing.setStatus(req.getStatus());
        existing.setInstructor(instructor);
        return mapToCourseResponse(courseRepository.save(existing));
    }

    public CourseResponse deleteCourseById(Long id) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        courseRepository.delete(existing);
        return mapToCourseResponse(existing);
    }
}
