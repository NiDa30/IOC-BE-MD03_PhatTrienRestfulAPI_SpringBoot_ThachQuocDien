package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PageResponse<StudentResponse> searchStudents(int page, int size, String sortBy, Sort.Direction direction, String keyword) {
        if (page < 0) {
            page = 0;
        }
        Sort sort;
        if (direction != null && sortBy != null && !sortBy.trim().isEmpty()) {
            sort = Sort.by(direction, sortBy.trim());
        } else {
            sort = Sort.unsorted();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        Page<StudentResponse> studentPage = studentRepository.searchStudents(searchKeyword, pageable);
        return PageResponse.of(studentPage);
    }
}
