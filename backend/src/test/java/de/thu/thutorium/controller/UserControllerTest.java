package de.thu.thutorium.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.thu.thutorium.model.User;
import de.thu.thutorium.model.UserRole;
import de.thu.thutorium.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  @Test
  public void testGetStudentCount() throws Exception {
    // Mock behavior of the UserService
    when(userService.getStudentCount()).thenReturn(42L);

    // Perform the GET request and verify the response
    mockMvc
        .perform(get("/students/count"))
        .andExpect(status().isOk())
        .andExpect(content().string("42"));

    // Verify interaction with the mock
    verify(userService, times(1)).getStudentCount();
  }

  @Test
  public void testGetTutorCount() throws Exception {
    // Mock behavior of the UserService
    when(userService.getTutorCount()).thenReturn(15L);

    // Perform the GET request and verify the response
    mockMvc
        .perform(get("/tutors/count"))
        .andExpect(status().isOk())
        .andExpect(content().string("15"));

    // Verify interaction with the mock
    verify(userService, times(1)).getTutorCount();
  }

  @Test
  void testGetAccount() throws Exception {
    Long userId = 1L;

    // Mock a User object
    User user = new User();
    user.setUserId(userId);
    user.setFirstName("John");
    user.setLastName("Doe");

    // Mock behavior of the UserService
    when(userService.findByUserId(userId)).thenReturn(user);

    // Perform the GET request and verify the response
    mockMvc
            .perform(get("/account").param("userId", String.valueOf(userId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"));

    // Verify interaction with the mock
    verify(userService, times(1)).findByUserId(userId);
  }

  @Test
  void testGetTutor() throws Exception {
    Long tutorId = 2L;

    // Mock a User object representing a tutor
    User tutor = new User();
    tutor.setUserId(tutorId);
    tutor.setFirstName("Jane");
    tutor.setLastName("Smith");
    tutor.setRole(UserRole.TUTOR);

    // Mock behavior of the UserService
    when(userService.getTutorByID(tutorId)).thenReturn(tutor);

    // Perform the GET request and verify the response
    mockMvc
            .perform(get("/tutor").param("tutorId", String.valueOf(tutorId)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(2))
            .andExpect(jsonPath("$.firstName").value("Jane"))
            .andExpect(jsonPath("$.lastName").value("Smith"))
            .andExpect(jsonPath("$.role").value("TUTOR"));

    // Verify interaction with the mock
    verify(userService, times(1)).getTutorByID(tutorId);
  }
}
