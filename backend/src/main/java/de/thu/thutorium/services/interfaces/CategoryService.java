package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import org.springframework.stereotype.Service;

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
}
