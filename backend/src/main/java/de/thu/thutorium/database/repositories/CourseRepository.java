package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.CourseDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link CourseDBO} entities. This interface extends {@link
 * JpaRepository} and provides custom query methods to interact with the underlying database,
 * particularly for finding courses based on the tutor's name.
 */
public interface CourseRepository extends JpaRepository<CourseDBO, Long> {

  /**
   * Finds courses where a participant with a specific first and last name has the "Tutor" role.
   *
   * @param firstName The participant's first name.
   * @param lastName The participant's last name.
   * @return A list of {@link CourseDBO} objects where the participant has the "Tutor" role.
   */
  @Query("SELECT c FROM CourseDBO c JOIN c.students s JOIN s.roles r "
          + "WHERE r.roleName = 'TUTOR' "
          + "AND LOWER(s.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) "
          + "AND LOWER(s.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
  List<CourseDBO> findByTutorFirstNameAndLastName(
          @Param("firstName") String firstName, @Param("lastName") String lastName);

  /**
   * Finds courses where a participant with a specific full name has the "Tutor" role.
   *
   * @param tutorName The full name of the tutor (e.g., "John Doe").
   * @return A list of {@link CourseDBO} objects where the participant has the "Tutor" role.
   */
  @Query(
          "SELECT c FROM CourseDBO c JOIN c.students s JOIN s.roles r "
                  + "WHERE r.roleName = 'TUTOR' "
                  + "AND (LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :tutorName, '%')) "
                  + "OR LOWER(CONCAT(s.lastName, ' ', s.firstName)) LIKE LOWER(CONCAT('%', :tutorName, '%')))")
  List<CourseDBO> findByTutorFullName(@Param("tutorName") String tutorName);

  /**
   * Finds courses by a partial match on the course name.
   *
   * @param name The partial course name to search for.
   * @return A list of {@link CourseDBO} objects with names that match the specified partial name.
   */
  @Query("SELECT c FROM CourseDBO c WHERE LOWER(c.courseName) LIKE LOWER(CONCAT('%', :name, '%'))")
  List<CourseDBO> findCourseByName(@Param("name") String name);

  /**
   * Finds a course by its ID.
   *
   * @param courseId The ID of the course to search for.
   * @return The {@link CourseDBO} with the specified ID.
   */
  Optional<CourseDBO> findCourseDBOByCourseId(Long courseId);

  /**
   * Finds courses by matching the category name.
   *
   * @param categoryName The name of the category to search for.
   * @return A list of {@link CourseDBO} objects that belong to the specified category.
   */
  @Query(
          "SELECT c FROM CourseDBO c JOIN c.courseCategories cc WHERE LOWER(cc.categoryName) = LOWER(:categoryName)")
  List<CourseDBO> findCoursesByCategoryName(@Param("categoryName") String categoryName);

  /**
   * Checks if a course exists in the database from its name.
   * @param courseName to be searched
   * @return {@code boolean} value indicating if the searched course exists
   */
  boolean existsByCourseName(String courseName);

}
