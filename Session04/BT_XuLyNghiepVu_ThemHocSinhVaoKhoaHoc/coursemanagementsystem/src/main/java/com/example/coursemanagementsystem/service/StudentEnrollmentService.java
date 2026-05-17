package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.EnrollCourseRequest;
import com.example.coursemanagementsystem.dto.EnrollmentDetail;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.model.StudentEnrollment;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.StudentEnrollmentRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
import com.example.coursemanagementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentEnrollmentService {
    private final StudentEnrollmentRepository studentEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentEnrollmentService(
            StudentEnrollmentRepository studentEnrollmentRepository,
            CourseRepository courseRepository,
            InstructorRepository instructorRepository,
            StudentRepository studentRepository
    ) {
        this.studentEnrollmentRepository = studentEnrollmentRepository;
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
    }

    public List<StudentEnrollment> getAllEnrollment() {
        return studentEnrollmentRepository.findAll();
    }

    public StudentEnrollment getEnrollmentById(Long id) {
        return studentEnrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudentEnrollment not found"));
    }

    public StudentEnrollment createEnrollment(StudentEnrollment enrollment) {
        if (enrollment.getCourse() != null && enrollment.getCourse().getId() != null) {
            courseRepository.findById(enrollment.getCourse().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Course with ID " + enrollment.getCourse().getId() + " does not exist."));
        }
        if (enrollment.getStudent() != null && enrollment.getStudent().getId() != null) {
            studentRepository.findById(enrollment.getStudent().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Student with ID " + enrollment.getStudent().getId() + " does not exist."));
        }
        return studentEnrollmentRepository.save(enrollment);
    }

    public StudentEnrollment updateEnrollment(Long id, StudentEnrollment enrollment) {
        if (enrollment.getCourse() != null && enrollment.getCourse().getId() != null) {
            courseRepository.findById(enrollment.getCourse().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Course with ID " + enrollment.getCourse().getId() + " does not exist."));
        }
        if (enrollment.getStudent() != null && enrollment.getStudent().getId() != null) {
            studentRepository.findById(enrollment.getStudent().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Student with ID " + enrollment.getStudent().getId() + " does not exist."));
        }
        StudentEnrollment existing = getEnrollmentById(id);
        existing.setStudent(enrollment.getStudent());
        existing.setCourse(enrollment.getCourse());
        return studentEnrollmentRepository.save(existing);
    }

    public StudentEnrollment deleteEnrollmentById(Long id) {
        StudentEnrollment existing = getEnrollmentById(id);
        studentEnrollmentRepository.delete(existing);
        return existing;
    }

    public StudentEnrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " does not exist."));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + courseId + " does not exist."));

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return studentEnrollmentRepository.save(enrollment);
    }

    public EnrollmentDetail enrollCourse(EnrollCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new IllegalArgumentException("Cannot enroll in an inactive course");
        }

        if (course.getInstructor() != null && course.getInstructor().getId() != null) {
            instructorRepository.findById(course.getInstructor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Instructor not found"));
        }

        Student student = studentRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(request.getStudentName()))
                .findFirst()
                .orElseGet(() -> {
                    Student newStudent = new Student();
                    newStudent.setName(request.getStudentName());
                    newStudent.setEmail(request.getStudentName().toLowerCase().replace(" ", ".") + "@gmail.com");
                    return studentRepository.save(newStudent);
                });

        StudentEnrollment enrollment = new StudentEnrollment();
        enrollment.setId(request.getId());
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        StudentEnrollment savedEnrollment = studentEnrollmentRepository.save(enrollment);

        EnrollmentDetail detail = new EnrollmentDetail();
        detail.setId(savedEnrollment.getId());
        detail.setStudentName(savedEnrollment.getStudent().getName());
        detail.setCourse(course);

        return detail;
    }
}
