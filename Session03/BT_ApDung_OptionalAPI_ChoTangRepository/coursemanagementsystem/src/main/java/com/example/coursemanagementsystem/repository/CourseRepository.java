package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    private final List<Course> courses = new ArrayList<>();

    public CourseRepository() {
        courses.add(new Course(1L, "Java Restful API Development", "Active", 1L));
        courses.add(new Course(2L, "Spring Boot Advanced", "Active", 1L));
        courses.add(new Course(3L, "Introduction to IoT", "Draft", 2L));
    }

    public List<Course> findAll() {
        return courses;
    }

    public Optional<Course> findById(Long id) {
        return courses.stream()
                .filter(course -> course.getId().equals(id))
                .findFirst();
    }

    public Course create(Course course) {
        if (course.getId() == null) {
            long newId = courses.stream()
                    .mapToLong(Course::getId)
                    .max()
                    .orElse(0L) + 1;
            course.setId(newId);
        } else {
            Optional<Course> existingOpt = findById(course.getId());
            if (existingOpt.isPresent()) {
                Course existing = existingOpt.get();
                existing.setTitle(course.getTitle());
                existing.setStatus(course.getStatus());
                existing.setInstructorId(course.getInstructorId());
                return existing;
            }
        }
        courses.add(course);
        return course;
    }

    public Course update(Long id, Course updatedCourse) {
        Course existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        existing.setTitle(updatedCourse.getTitle());
        existing.setStatus(updatedCourse.getStatus());
        existing.setInstructorId(updatedCourse.getInstructorId());
        return existing;
    }

    public Course deleteById(Long id) {
        Course existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        courses.remove(existing);
        return existing;
    }
}
