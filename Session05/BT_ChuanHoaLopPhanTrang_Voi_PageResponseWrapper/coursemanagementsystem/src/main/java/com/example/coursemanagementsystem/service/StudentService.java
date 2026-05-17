package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        if (student.getEmail() == null || student.getEmail().isEmpty()) {
            student.setEmail(student.getName().toLowerCase().replace(" ", ".") + "@gmail.com");
        }
        return studentRepository.save(student);
    }
}
