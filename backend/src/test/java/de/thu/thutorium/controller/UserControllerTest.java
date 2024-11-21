package de.thu.thutorium.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
}
