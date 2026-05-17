package com.example.coursemanagementsystem.controller;

import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseInstructorResponse;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    void testGetAllCourses_Success() throws Exception {
        CourseResponse courseResponse = new CourseResponse(
                10L,
                "Spring Boot Masterclass",
                CourseStatus.ACTIVE,
                new CourseInstructorResponse(1L, "John Doe")
        );
        Page<CourseResponse> page = new PageImpl<>(List.of(courseResponse), PageRequest.of(0, 2), 1);

        when(courseService.getPagedCourses(anyInt(), anyInt(), anyString(), any(Sort.Direction.class)))
                .thenReturn(page);

        mockMvc.perform(get("/courses")
                .param("page", "0")
                .param("size", "2")
                .param("sortBy", "title")
                .param("direction", "ASC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.content[0].id").value(10))
                .andExpect(jsonPath("$.data.content[0].title").value("Spring Boot Masterclass"))
                .andExpect(jsonPath("$.data.content[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.content[0].instructor.id").value(1))
                .andExpect(jsonPath("$.data.content[0].instructor.name").value("John Doe"));
    }
}
