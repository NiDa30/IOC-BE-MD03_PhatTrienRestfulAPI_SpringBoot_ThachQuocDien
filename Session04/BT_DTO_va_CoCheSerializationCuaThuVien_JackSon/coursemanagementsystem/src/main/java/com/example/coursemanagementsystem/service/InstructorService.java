package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.InstructorCreateRequest;
import com.example.coursemanagementsystem.dto.InstructorDetail;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.repository.StudentEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final StudentEnrollmentRepository studentEnrollmentRepository;

    @Autowired
    public InstructorService(
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            StudentEnrollmentRepository studentEnrollmentRepository
    ) {
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
    }

    public List<Instructor> getAllInstructor() {
        return instructorRepository.findAll();
    }

    public Instructor getInstructorById(Long id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
    }

    public Instructor createInstructor(InstructorCreateRequest req) {
        Instructor instructor = new Instructor();
        instructor.setName(req.getName());
        instructor.setEmail(req.getEmail());
        return instructorRepository.save(instructor);
    }

    public Instructor updateInstructor(Long id, Instructor instructor) {
        Instructor existing = getInstructorById(id);
        existing.setName(instructor.getName());
        existing.setEmail(instructor.getEmail());
        return instructorRepository.save(existing);
    }

    public Instructor deleteInstructorById(Long id) {
        Instructor existing = getInstructorById(id);
        instructorRepository.delete(existing);
        return existing;
    }

    public List<InstructorDetail> getInstructorDetails() {
        List<Instructor> instructors = instructorRepository.findAll();
        List<Course> allCourses = courseRepository.findAll();
        List<StudentEnrollment> allEnrollments = studentEnrollmentRepository.findAll();

        return instructors.stream().map(instructor -> {
            List<Course> filteredCourses = allCourses.stream()
                    .filter(course -> course.getInstructor() != null && instructor.getId().equals(course.getInstructor().getId()))
                    .filter(course -> CourseStatus.ACTIVE == course.getStatus())
                    .filter(course -> allEnrollments.stream()
                            .anyMatch(enrollment -> enrollment.getCourse() != null && course.getId().equals(enrollment.getCourse().getId())))
                    .toList();

            return new InstructorDetail(
                    instructor.getId(),
                    instructor.getName(),
                    instructor.getEmail(),
                    filteredCourses
            );
        }).toList();
    }
}
