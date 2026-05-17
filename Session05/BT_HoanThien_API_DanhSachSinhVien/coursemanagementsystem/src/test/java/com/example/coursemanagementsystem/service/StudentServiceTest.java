package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.model.Student;
import com.example.coursemanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("Student 1");
        student.setEmail("student.1@gmail.com");
    }

    @Test
    void testCreateStudent_NullEmail_GeneratesEmail() {
        Student newStudent = new Student();
        newStudent.setName("Alice Smith");

        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student result = studentService.createStudent(newStudent);

        assertNotNull(result);
        assertEquals("alice.smith@gmail.com", result.getEmail());
        verify(studentRepository).save(newStudent);
    }

    @Test
    void testSearchStudents_Success_AllFilters() {
        StudentResponse responseDto = new StudentResponse(1L, "Student 1");
        Page<StudentResponse> studentPage = new PageImpl<>(List.of(responseDto));
        when(studentRepository.searchStudents(eq("Student 1"), any(Pageable.class))).thenReturn(studentPage);

        PageResponse<StudentResponse> result = studentService.searchStudents(0, 10, "name", Sort.Direction.ASC, "Student 1");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Student 1", result.getItems().get(0).getName());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(studentRepository).searchStudents(eq("Student 1"), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "name"), capturedPageable.getSort());
    }

    @Test
    void testSearchStudents_Success_NullDirection_Unsorted() {
        StudentResponse responseDto = new StudentResponse(1L, "Student 1");
        Page<StudentResponse> studentPage = new PageImpl<>(List.of(responseDto));
        when(studentRepository.searchStudents(eq(null), any(Pageable.class))).thenReturn(studentPage);

        PageResponse<StudentResponse> result = studentService.searchStudents(0, 10, "name", null, "   ");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(studentRepository).searchStudents(eq(null), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertTrue(capturedPageable.getSort().isUnsorted());
    }
}
