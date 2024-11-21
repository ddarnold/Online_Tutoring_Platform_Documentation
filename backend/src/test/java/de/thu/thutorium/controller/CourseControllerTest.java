package de.thu.thutorium.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.model.User;
import de.thu.thutorium.model.Category;
import de.thu.thutorium.service.CourseService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CourseService courseService; // MockBean for Spring context injection

  private Course sampleCourse;

  @BeforeEach
  public void setUp() {
    // Create a mock User (tutor)
    User tutor = new User();
    tutor.setUserId(1L);
    tutor.setFirstName("John");
    tutor.setLastName("Doe");

    // Create a mock Category
    Category category = new Category();
    category.setCategoryId(1L);
    category.setCategoryName("Science");

    // Create a mock Course
    sampleCourse =
        new Course(
            1L,
            tutor,
            "Mathematics",
            "A short description",
            "A long description",
            LocalDateTime.now(),
            LocalDate.of(2024, 1, 10),
            LocalDate.of(2024, 6, 15),
            category,
            Collections.emptyList());
  }

  @Test
  public void testGetCoursesByTutor_withFirstNameAndLastName() throws Exception {
    String firstName = "John";
    String lastName = "Doe";

    when(courseService.findCoursesByTutorName(firstName, lastName))
        .thenReturn(Collections.singletonList(sampleCourse));

    mockMvc
        .perform(get("/courses/tutor").param("firstName", firstName).param("lastName", lastName))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].courseId").value(1))
        .andExpect(jsonPath("$[0].courseName").value("Mathematics"))
        .andExpect(jsonPath("$[0].tutor.firstName").value("John"));

    verify(courseService, times(1)).findCoursesByTutorName(firstName, lastName);
  }

  @Test
  public void testGetCoursesByName() throws Exception {
    String partialName = "Math";

    when(courseService.findCoursesByName(partialName))
        .thenReturn(Collections.singletonList(sampleCourse));

    mockMvc
        .perform(get("/courses/search").param("name", partialName))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].courseId").value(1))
        .andExpect(jsonPath("$[0].courseName").value("Mathematics"));

    verify(courseService, times(1)).findCoursesByName(partialName);
  }
}
