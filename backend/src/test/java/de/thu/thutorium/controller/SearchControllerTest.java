package de.thu.thutorium.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.model.User;
import de.thu.thutorium.model.UserRole;
import de.thu.thutorium.service.SearchService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SearchService searchService;

  private User mockTutor;
  private Course mockCourse;

  @BeforeEach
  public void setUp() {
    // Mock User (Tutor)
    mockTutor =
        new User(
            1L,
            "John",
            "Doe",
            UserRole.TUTOR,
            true,
            LocalDateTime.now(),
            Collections.emptyList(),
            Collections.emptyList(),
            null);

    // Mock Course
    mockCourse =
        new Course(
            1L,
            mockTutor,
            "Mathematics",
            "A short description",
            "A long description",
            LocalDateTime.now(),
            null,
            null,
            null,
            Collections.emptyList());
  }

  @Test
  public void testSearchTutorsOnly() throws Exception {
    String tutorName = "John";

    when(searchService.searchTutors(tutorName)).thenReturn(List.of(mockTutor));
    when(searchService.searchCourses(anyString())).thenReturn(Collections.emptyList());

    mockMvc
        .perform(get("/search").param("tutorName", tutorName))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].userId").value(1))
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[0].lastName").value("Doe"));

    verify(searchService, times(1)).searchTutors(tutorName);
    verify(searchService, times(0)).searchCourses(anyString());
  }

  @Test
  public void testSearchCoursesOnly() throws Exception {
    String courseName = "Math";

    when(searchService.searchTutors(anyString())).thenReturn(Collections.emptyList());
    when(searchService.searchCourses(courseName)).thenReturn(List.of(mockCourse));

    mockMvc
        .perform(get("/search").param("courseName", courseName))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].courseId").value(1))
        .andExpect(jsonPath("$[0].courseName").value("Mathematics"));

    verify(searchService, times(1)).searchCourses(courseName);
    verify(searchService, times(0)).searchTutors(anyString());
  }

  @Test
  public void testSearchTutorsAndCourses() throws Exception {
    String tutorName = "John";
    String courseName = "Math";

    when(searchService.searchTutors(tutorName)).thenReturn(List.of(mockTutor));
    when(searchService.searchCourses(courseName)).thenReturn(List.of(mockCourse));

    mockMvc
        .perform(get("/search").param("tutorName", tutorName).param("courseName", courseName))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].userId").value(1))
        .andExpect(jsonPath("$[1].courseId").value(1));

    verify(searchService, times(1)).searchTutors(tutorName);
    verify(searchService, times(1)).searchCourses(courseName);
  }

  @Test
  public void testSearchNoParameters() throws Exception {
    when(searchService.searchTutors(anyString())).thenReturn(Collections.emptyList());
    when(searchService.searchCourses(anyString())).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/search")).andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());

    verify(searchService, times(0)).searchTutors(anyString());
    verify(searchService, times(0)).searchCourses(anyString());
  }
}
