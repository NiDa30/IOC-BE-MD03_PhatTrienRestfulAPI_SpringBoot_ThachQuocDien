package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.PageResponse;
import com.example.coursemanagementsystem.model.Course;
import com.example.coursemanagementsystem.model.CourseStatus;
import com.example.coursemanagementsystem.model.Instructor;
import com.example.coursemanagementsystem.repository.CourseRepository;
import com.example.coursemanagementsystem.repository.InstructorRepository;
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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private Instructor instructor;

    @BeforeEach
    void setUp() {
        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setName("John Doe");

        course = new Course();
        course.setId(10L);
        course.setTitle("Java Programming");
        course.setStatus(CourseStatus.ACTIVE);
        course.setInstructor(instructor);
    }

    @Test
    void testGetPagedCourses_PageLessThanZero_AdjustsToZero() {
        Page<Course> coursePage = new PageImpl<>(List.of(course));
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponse> result = courseService.getPagedCourses(-5, 5, "title", Sort.Direction.ASC);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAll(pageableCaptor.capture());
        
        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(5, capturedPageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "title"), capturedPageable.getSort());
    }

    @Test
    void testGetPagedCourses_NullSortBy_DefaultsToId() {
        Page<Course> coursePage = new PageImpl<>(List.of(course));
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponse> result = courseService.getPagedCourses(0, 10, null, Sort.Direction.DESC);

        assertNotNull(result);
        
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAll(pageableCaptor.capture());
        
        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals("id", capturedPageable.getSort().getOrderFor("id").getProperty());
        assertEquals(Sort.Direction.DESC, capturedPageable.getSort().getOrderFor("id").getDirection());
    }

    @Test
    void testGetPagedCourses_EmptySortBy_DefaultsToId() {
        Page<Course> coursePage = new PageImpl<>(List.of(course));
        when(courseRepository.findAll(any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponse> result = courseService.getPagedCourses(2, 20, "   ", Sort.Direction.ASC);

        assertNotNull(result);
        
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAll(pageableCaptor.capture());
        
        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(2, capturedPageable.getPageNumber());
        assertEquals(20, capturedPageable.getPageSize());
        assertEquals("id", capturedPageable.getSort().getOrderFor("id").getProperty());
        assertEquals(Sort.Direction.ASC, capturedPageable.getSort().getOrderFor("id").getDirection());
    }
}
