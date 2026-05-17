package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course createCourse(Course course) {
        if (course.getInstructor() != null && course.getInstructor().getId() != null) {
            instructorRepository.findById(course.getInstructor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Instructor with ID " + course.getInstructor().getId() + " does not exist."));
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course course) {
        if (course.getInstructor() != null && course.getInstructor().getId() != null) {
            instructorRepository.findById(course.getInstructor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Instructor with ID " + course.getInstructor().getId() + " does not exist."));
        }
        Course existing = getCourseById(id);
        existing.setTitle(course.getTitle());
        existing.setStatus(course.getStatus());
        existing.setInstructor(course.getInstructor());
        return courseRepository.save(existing);
    }

    public Course deleteCourseById(Long id) {
        Course existing = getCourseById(id);
        courseRepository.delete(existing);
        return existing;
    }
}
