package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryService is an interface for managing course categories.
 *
 * <p>This service provides methods to create and manage course categories within the system.
 */
@Service
public interface CategoryService {
  /**
   * Creates a new course category based on the provided category data.
   *
   * @param courseCategoryTO the {@link CourseCategoryTO} object containing the course category
   *     data.
   * @return the created {@link CourseCategoryTO} object representing the newly created course
   *     category.
   */
  CourseCategoryTO createCourseCategory(CourseCategoryTO courseCategoryTO);

  CourseCategoryTO updateCourseCategory(Long categoryId, CourseCategoryTO courseCategoryTO);

  void deleteCourseCategory(int categoryId);

  /**
   * Retrieves all available course categories.
   *
   * <p>This method will return a list of {@link CourseCategoryTO} objects, each representing a
   * distinct category to which courses can belong.
   *
   * @return a list of {@link CourseCategoryTO} objects representing all available categories. If no
   *     categories exist, an empty list is returned.
   */
  List<CourseCategoryTO> getAllCategories();
}
