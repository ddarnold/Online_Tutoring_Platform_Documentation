package de.thu.thutorium.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.thu.thutorium.model.Category;
import de.thu.thutorium.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

  @Autowired private MockMvc mockMvc;

  // Use @MockBean to mock the CategoryService
  @MockBean private CategoryService categoryService;

  private List<Category> mockCategories;

  @BeforeEach
  void setUp() {
    // Create mock data
    mockCategories =
        Arrays.asList(
            new Category(1L, "Programming"),
            new Category(2L, "Mathematics"),
            new Category(3L, "Science"));
  }

  @Test
  void testGetAllCategories() throws Exception {
    // Mock the service behavior
    when(categoryService.getAllCategories()).thenReturn(mockCategories);

    // Perform the GET request and verify the response
    mockMvc
        .perform(get("/search/categories").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(mockCategories.size()))
        .andExpect(jsonPath("$[0].categoryId").value(1))
        .andExpect(jsonPath("$[0].categoryName").value("Programming"))
        .andExpect(jsonPath("$[1].categoryId").value(2))
        .andExpect(jsonPath("$[1].categoryName").value("Mathematics"))
        .andExpect(jsonPath("$[2].categoryId").value(3))
        .andExpect(jsonPath("$[2].categoryName").value("Science"));

    // Verify the service was called once
    verify(categoryService, times(1)).getAllCategories();
  }
}
