package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link CourseCategoryDBO} entities.
 *
 * <p>This interface extends {@link JpaRepository}, which provides various methods for interacting
 * with the database, such as saving, finding, deleting, and updating entities. Additionally, custom
 * query methods can be defined to suit specific requirements.
 *
 * <p>By using {@code @Repository}, Spring treats this interface as a Data Access Object (DAO),
 * enabling exception translation into Spring's DataAccessException hierarchy.
 */
@Repository
public interface CategoryRepository extends JpaRepository<CourseCategoryDBO, Long> {

  /**
   * Finds a category by its unique name.
   *
   * <p>This method retrieves a single {@link CourseCategoryDBO} entity based on its unique name.
   * The search is case-sensitive.
   *
   * @param categoryName the unique name of the category to retrieve.
   * @return the {@link CourseCategoryDBO} entity matching the provided name, or {@code null} if no
   *     match is found.
   */
  Optional<CourseCategoryDBO> findCourseCategoryDBOByCategoryName(String categoryName);

  /**
   * Finds all categories that are associated with at least one course.
   *
   * <p>This method retrieves a list of {@link CourseCategoryDBO} entities that are associated with
   * one or more {@link de.thu.thutorium.database.dbObjects.CourseDBO} entities. Categories without courses are excluded.
   *
   * @return a {@link List} of {@link CourseCategoryDBO} objects associated with courses.
   */
  @Query("SELECT DISTINCT c FROM CourseCategoryDBO c JOIN c.courses courses")
  List<CourseCategoryDBO> findCategoriesWithCourses();

  boolean existsByCategoryName(String categoryName);
}
