package de.thu.thutorium.controller;

import de.thu.thutorium.model.Category;
import de.thu.thutorium.model.Course;
import de.thu.thutorium.service.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing categories in the system. This controller provides endpoints for
 * retrieving category information.
 *
 * <p>The {@link CategoryController} class uses the {@link CategoryService} to fetch category data
 * and expose it through RESTful endpoints. It is annotated with {@link RestController} to indicate
 * that it is a Spring MVC controller for handling HTTP requests.
 *
 * @see CategoryService
 * @see Category
 */
@RestController
public class CategoryController {

  /**
   * Service responsible for handling category-related business logic. This service is injected into
   * the controller using {@code @Autowired}.
   */
  @Autowired private CategoryService categoryService;

  /**
   * Endpoint for retrieving all categories.
   *
   * <p>This method is mapped to a GET request at the URL {@code /search/categories}. It returns a
   * list of all {@link Category} objects available in the system.
   *
   * <p>The endpoint is configured with {@code @CrossOrigin} to allow cross-origin requests from the
   * specified frontend domain ({@code http://localhost:3000}), with a cache duration of 3600
   * seconds (1 hour).
   *
   * @return a {@link List} of {@link Category} objects representing all categories.
   */
  @GetMapping("search/categories")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  /**
   * REST controller for managing courses related to categories. This endpoint is used to fetch
   * courses that belong to a specific category.
   *
   * <p>The {@link CategoryController} class uses the {@link CategoryService} to fetch courses based
   * on the category name and exposes this functionality through a RESTful endpoint. It is mapped to
   * {@code GET /courses/category/{categoryName}} for retrieving courses by category name.
   *
   * @param categoryName The name of the category for which courses are retrieved.
   * @return a {@link List} of {@link Course} objects belonging to the specified category.
   */
  @GetMapping("/courses/category/{categoryName}")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public List<Course> getCoursesByCategory(@PathVariable String categoryName) {
    return categoryService.getCoursesByCategory(categoryName);
  }
}
