package de.thu.thutorium;

import de.thu.thutorium.api.controllers.SearchController;
import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.TutorTO;
import de.thu.thutorium.services.interfaces.CourseService;
import de.thu.thutorium.services.interfaces.SearchService;
import de.thu.thutorium.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchCategoryTest {

    @Mock
    private SearchService searchService;

    @Mock
    private CourseService courseService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchTutorsAndCourses() {
        // Mock data
        TutorTO mockTutor = new TutorTO();
        CourseTO mockCourse = new CourseTO();

        when(searchService.searchTutors("John")).thenReturn(Collections.singletonList(mockTutor));

        // Call the search method
        List<Object> results = Collections.singletonList(searchController.search("John", "Java"));

        // Verify
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.contains(mockTutor));
        assertTrue(results.contains(mockCourse));

        verify(searchService, times(1)).searchTutors("John");
        verify(searchService, times(1)).searchTutors("Java");
    }

    @Test
    void testSearchEmptyParameters() {
        // Call the search method with empty parameters
        List<Object> results = Collections.singletonList(searchController.search(null, null));

        // Verify
        assertNotNull(results);
        assertTrue(results.isEmpty());

        verifyNoInteractions(searchService);
    }

//    @Test
//    void testGetCategories() {
//        // Mock data
//        CourseCategoryTO mockCategory = new CourseCategoryTO();
//
//        when(searchService.getAllCategories()).thenReturn(Collections.singletonList(mockCategory));
//
//        // Call the method
//        List<CourseCategoryTO> categories = searchController.getCategories();
//
//        // Verify
//        assertNotNull(categories);
//        assertEquals(1, categories.size());
//        assertEquals("Programming", categories.get(0).getCategoryName());
//
//        verify(searchService, times(1)).getAllCategories();
//    }
//
//    @Test
//    void testGetCoursesByCategory() {
//        // Mock data
//        CourseTO mockCourse = new CourseTO(1L, "Java Basics", 1L, "Intro course", "Detailed course", null, null, 4.8);
//
//        when(courseService.getCoursesByCategory("Programming")).thenReturn(Collections.singletonList(mockCourse));
//
//        // Call the method
//        List<CourseTO> courses = searchController.getCoursesByCategory("Programming");
//
//        // Verify
//        assertNotNull(courses);
//        assertEquals(1, courses.size());
//        assertEquals("Java Basics", courses.get(0).getCourseName());
//
//        verify(courseService, times(1)).getCoursesByCategory("Programming");
//    }

    @Test
    void testGetStudentCount() {
        // Mock data
        when(userService.getStudentCount()).thenReturn(42L);

        // Call the method
        Long count = searchController.getStudentCount();

        // Verify
        assertNotNull(count);
        assertEquals(42L, count);

        verify(userService, times(1)).getStudentCount();
    }

    @Test
    void testGetTutorsCount() {
        // Mock data
        when(userService.getTutorCount()).thenReturn(15L);

        // Call the method
        Long count = searchController.getTutorsCount();

        // Verify
        assertNotNull(count);
        assertEquals(15L, count);

        verify(userService, times(1)).getTutorCount();
    }

    @Test
    void testGetCoursesCount() {
        // Mock data
        when(courseService.getTotalCountOfCourses()).thenReturn(10L);

        // Call the method
        Long count = searchController.getCoursesCount();

        // Verify
        assertNotNull(count);
        assertEquals(10L, count);

        verify(courseService, times(1)).getTotalCountOfCourses();
    }
}
