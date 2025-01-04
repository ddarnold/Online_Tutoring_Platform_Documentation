package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.CourseCategoryTOMapper;
import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import de.thu.thutorium.database.DBOMappers.CategoryDBOMapper;
import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import de.thu.thutorium.database.repositories.CategoryRepository;
import de.thu.thutorium.exceptions.ResourceAlreadyExistsException;
import de.thu.thutorium.exceptions.ResourceNotFoundException;
import de.thu.thutorium.services.interfaces.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service implementation for managing course categories.
 *
 * <p>This service provides functionality to create new course categories and interact with the
 * underlying data store. It interacts with the {@link CategoryRepository} to perform database
 * operations and uses the {@link CourseCategoryTOMapper} to map between the data transfer objects
 * (DTO) and the database objects (DBO).
 *
 * <p>This class provides a method to create a new course category, including setting the creation
 * timestamp.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
  private final CategoryRepository courseCategoryRepository;
  private final CategoryDBOMapper categoryDBOMapper;
  private final CourseCategoryTOMapper courseCategoryTOMapper;

  public CategoryServiceImpl(
      CategoryRepository courseCategoryRepository,
      CategoryDBOMapper categoryDBOMapper,
      CourseCategoryTOMapper courseCategoryTOMapper) {
    this.courseCategoryRepository = courseCategoryRepository;
    this.categoryDBOMapper = categoryDBOMapper;
    this.courseCategoryTOMapper = courseCategoryTOMapper;
  }

  /**
   * Creates a new course category.
   *
   * @param courseCategory the data transfer object containing the details of the course category to
   *     be created
   * @return the created course category as a {@link CourseCategoryTO}
   * @throws ResourceAlreadyExistsException if the category already exists by name
   */
  @Override
  public CourseCategoryTO createCourseCategory(@Valid CourseCategoryTO courseCategory) {
    // Check if the courseCategoryDBO already exists (by name)
    Optional<CourseCategoryDBO> categoryDBOOptional =
            courseCategoryRepository.findCourseCategoryDBOByCategoryName(
                courseCategory.getCategoryName());
    categoryDBOOptional.ifPresent(
        (category) -> {
          throw new ResourceAlreadyExistsException(
              "Category \"" + category.getCategoryName() + "\" already exists!");
        });
    CourseCategoryDBO categoryDBO = categoryDBOMapper.toDBO(courseCategory);
    CourseCategoryDBO savedCategoryDBO = courseCategoryRepository.save(categoryDBO);
    return courseCategoryTOMapper.toDTO(savedCategoryDBO);
  }

  /**
   * Updates an existing course category.
   *
   * @param categoryId the ID of the course category to be updated
   * @param courseCategory the data transfer object containing the updated details of the course
   *     category
   * @return an {@link CourseCategoryTO} containing the updated course category.
   * @throws ResourceNotFoundException, if the searched category does not exist in the database.
   */
  @Override
  public CourseCategoryTO updateCourseCategory(Long categoryId, @Valid CourseCategoryTO courseCategory) {
    Optional<CourseCategoryDBO> courseCategoryOptional = courseCategoryRepository.findById(categoryId);
    CourseCategoryDBO existingCategory = courseCategoryOptional.orElseThrow(
            () -> new ResourceNotFoundException("Error: Course Category with id "
            + categoryId + " not found!"));
    existingCategory.setCategoryName(courseCategory.getCategoryName());
    existingCategory.setCreatedOn(LocalDateTime.now());
    CourseCategoryDBO savedCategoryDBO = courseCategoryRepository.save(existingCategory);
    return courseCategoryTOMapper.toDTO(savedCategoryDBO);
  }

  /**
   * Deletes a course category by its ID.
   *
   * @param categoryId the ID of the course category to be deleted
   */
  @Override
  public void deleteCourseCategory(int categoryId) {
    // Check if the courseCategory exists => throw Not found exception
    // Delete the courseCategoryDBO => Check for side effects on courses, other possible side
    // effects
  }
}
