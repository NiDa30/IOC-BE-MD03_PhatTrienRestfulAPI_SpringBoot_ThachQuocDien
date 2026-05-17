package com.example.coursemanagementsystem.service;

import com.example.coursemanagementsystem.dto.CourseResponse;
import com.example.coursemanagementsystem.dto.CourseResponseV2;
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

    @Test
    void testGetPagedCoursesByStatus_Success() {
        Page<Course> coursePage = new PageImpl<>(List.of(course));
        when(courseRepository.findAllByStatus(eq(CourseStatus.ACTIVE), any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponse> result = courseService.getPagedCoursesByStatus(0, 10, "title", Sort.Direction.ASC, CourseStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Java Programming", result.getItems().get(0).getTitle());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAllByStatus(eq(CourseStatus.ACTIVE), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "title"), capturedPageable.getSort());
    }

    @Test
    void testGetPagedCoursesByStatus_PageLessThanZero_AdjustsToZero() {
        Page<Course> coursePage = new PageImpl<>(List.of(course));
        when(courseRepository.findAllByStatus(eq(CourseStatus.ACTIVE), any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponse> result = courseService.getPagedCoursesByStatus(-5, 5, "title", Sort.Direction.ASC, CourseStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAllByStatus(eq(CourseStatus.ACTIVE), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(5, capturedPageable.getPageSize());
    }

    @Test
    void testGetPagedCoursesByStatusV2_Success() {
        CourseResponseV2 courseResponseV2 = new CourseResponseV2(10L, "Java Programming", CourseStatus.ACTIVE);
        Page<CourseResponseV2> coursePage = new PageImpl<>(List.of(courseResponseV2));
        when(courseRepository.findAllByStatusV2(eq(CourseStatus.ACTIVE), any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponseV2> result = courseService.getPagedCoursesByStatusV2(0, 10, "title", Sort.Direction.ASC, CourseStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Java Programming", result.getItems().get(0).getTitle());
        assertEquals(CourseStatus.ACTIVE, result.getItems().get(0).getStatus());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAllByStatusV2(eq(CourseStatus.ACTIVE), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "title"), capturedPageable.getSort());
    }

    @Test
    void testGetPagedCoursesByStatusV2_PageLessThanZero_AdjustsToZero() {
        CourseResponseV2 courseResponseV2 = new CourseResponseV2(10L, "Java Programming", CourseStatus.ACTIVE);
        Page<CourseResponseV2> coursePage = new PageImpl<>(List.of(courseResponseV2));
        when(courseRepository.findAllByStatusV2(eq(CourseStatus.ACTIVE), any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponseV2> result = courseService.getPagedCoursesByStatusV2(-5, 5, "title", Sort.Direction.ASC, CourseStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).findAllByStatusV2(eq(CourseStatus.ACTIVE), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(5, capturedPageable.getPageSize());
    }

    @Test
    void testSearchCourses_Success_AllFilters() {
        CourseResponseV2 courseResponseV2 = new CourseResponseV2(10L, "Java Programming", CourseStatus.ACTIVE);
        Page<CourseResponseV2> coursePage = new PageImpl<>(List.of(courseResponseV2));
        when(courseRepository.searchCourses(eq(CourseStatus.ACTIVE), eq("Java"), any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponseV2> result = courseService.searchCourses(0, 10, "title", Sort.Direction.ASC, CourseStatus.ACTIVE, "Java");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Java Programming", result.getItems().get(0).getTitle());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).searchCourses(eq(CourseStatus.ACTIVE), eq("Java"), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(0, capturedPageable.getPageNumber());
        assertEquals(10, capturedPageable.getPageSize());
        assertEquals(Sort.by(Sort.Direction.ASC, "title"), capturedPageable.getSort());
    }

    @Test
    void testSearchCourses_Success_NullDirection_Unsorted() {
        CourseResponseV2 courseResponseV2 = new CourseResponseV2(10L, "Java Programming", CourseStatus.ACTIVE);
        Page<CourseResponseV2> coursePage = new PageImpl<>(List.of(courseResponseV2));
        when(courseRepository.searchCourses(eq(null), eq(null), any(Pageable.class))).thenReturn(coursePage);

        PageResponse<CourseResponseV2> result = courseService.searchCourses(0, 10, "title", null, null, "   ");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(courseRepository).searchCourses(eq(null), eq(null), pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertTrue(capturedPageable.getSort().isUnsorted());
    }
}
