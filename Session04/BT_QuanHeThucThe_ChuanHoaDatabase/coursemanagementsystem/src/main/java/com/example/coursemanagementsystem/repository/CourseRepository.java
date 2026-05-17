package com.example.coursemanagementsystem.repository;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    private final List<Course> courses = new ArrayList<>();

    public CourseRepository() {
        Instructor mockInstructor1 = new Instructor(1L, "Thach Quoc Dien", "dien.tq@gmail.com");
        Instructor mockInstructor2 = new Instructor(2L, "Nguyen Van A", "a.nguyen@gmail.com");

        courses.add(new Course(1L, "Intro Java", CourseStatus.ACTIVE, mockInstructor1));
        courses.add(new Course(2L, "Spring Boot Advanced", CourseStatus.INACTIVE, mockInstructor1));
        courses.add(new Course(3L, "Introduction to IoT", CourseStatus.INACTIVE, mockInstructor2));
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
                existing.setInstructor(course.getInstructor());
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
        existing.setInstructor(updatedCourse.getInstructor());
        return existing;
    }

    public Course deleteById(Long id) {
        Course existing = findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        courses.remove(existing);
        return existing;
    }
}
