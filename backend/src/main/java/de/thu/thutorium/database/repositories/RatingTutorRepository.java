package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.RatingTutorDBO;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link RatingTutorDBO} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD (Create, Read, Update, Delete)
 * operations and query methods for {@link RatingTutorDBO} entities, which represent ratings given
 * by users for tutors.
 *
 * <p>The repository is annotated with {@link Repository}, indicating that it is a Spring Data JPA
 * repository and should be managed by the Spring container.
 *
 * <p>Custom query methods can be added here if needed, leveraging Spring Data JPA's query creation
 * mechanism.
 */
@Repository
public interface RatingTutorRepository extends JpaRepository<RatingTutorDBO, Long> {
  /**
   * Finds a list of RatingTutorDBO entities based on the tutor's user ID and the student's user ID.
   *
   * <p>This method retrieves all ratings given by a specific student to a specific tutor. The
   * results can be limited by specifying a limit parameter.
   *
   * @param tutorUserId The unique ID of the tutor.
   * @param studentUserId The unique ID of the student.
   * @param limit The maximum number of results to return.
   * @return A list of {@link RatingTutorDBO} objects representing the ratings given by the student
   *     to the tutor.
   */
  List<RatingTutorDBO> findByTutor_UserIdAndStudent_UserId(
      Long tutorUserId, Long studentUserId, Limit limit);
}
