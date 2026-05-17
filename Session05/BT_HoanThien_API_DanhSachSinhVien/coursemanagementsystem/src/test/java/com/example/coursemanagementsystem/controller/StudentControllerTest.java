package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.dto.StudentResponse;
import com.example.coursemanagementsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void testGetAllStudents_Success_AllParameters() throws Exception {
        StudentResponse studentResponse = new StudentResponse(1L, "Student 1");
        PageResponse<StudentResponse> pageResponse = new PageResponse<>(
                List.of(studentResponse),
                0,
                10,
                1,
                1,
                true
        );

        when(studentService.searchStudents(anyInt(), anyInt(), anyString(), any(Sort.Direction.class), eq("Student 1")))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/students")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "name")
                .param("direction", "ASC")
                .param("keyword", "Student 1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.items[0].id").value(1))
                .andExpect(jsonPath("$.data.items[0].name").value("Student 1"))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalItems").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    void testGetAllStudents_Success_UnsortedAndNoKeyword() throws Exception {
        StudentResponse studentResponse = new StudentResponse(1L, "Student 1");
        PageResponse<StudentResponse> pageResponse = new PageResponse<>(
                List.of(studentResponse),
                0,
                10,
                1,
                1,
                true
        );

        when(studentService.searchStudents(anyInt(), anyInt(), eq(null), eq(null), eq(null)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
