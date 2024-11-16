package de.thu.thutorium.service;

import de.thu.thutorium.model.Category;
import de.thu.thutorium.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing operations related to {@link Category} entities. This class acts as an
 * intermediary between the controller and the repository layers, providing business logic and
 * interaction with the database through the {@link CategoryRepository}.
 *
 * <p>This service is annotated with {@code @Service} to indicate that it's a Spring-managed service
 * bean. It leverages dependency injection to automatically inject an instance of {@link
 * CategoryRepository} using the {@code @Autowired} annotation.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @Autowired
 * private CategoryService categoryService;
 *
 * List<Category> categories = categoryService.getAllCategories();
 * }</pre>
 *
 * @see Category
 * @see CategoryRepository
 */
@Service
public class CategoryService {
  /** Repository for performing CRUD operations on {@link Category} entities. */
  @Autowired private CategoryRepository categoryRepository;

  /**
   * Retrieves a list of all available categories from the database.
   *
   * <p>This method uses the {@link CategoryRepository#findAllCategories()} method to fetch all
   * categories. It returns an empty list if no categories are found.
   *
   * @return a {@link List} of {@link Category} objects representing all categories.
   */
  public List<Category> getAllCategories() {
    return categoryRepository.findAllCategories();
  }
}
