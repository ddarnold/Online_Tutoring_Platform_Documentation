package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.RatingCourseDBO;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link RatingCourseDBO} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD (Create, Read, Update, Delete)
 * operations and query methods for {@link RatingCourseDBO} entities, which represent ratings given
 * by users for courses.
 *
 * <p>The repository is annotated with {@link Repository}, indicating that it is a Spring Data JPA
 * repository and should be managed by the Spring container.
 *
 * <p>Custom query methods can be added here if needed, leveraging Spring Data JPA's query creation
 * mechanism.
 */
@Repository
public interface RatingCourseRepository extends JpaRepository<RatingCourseDBO, Long> {
    List<RatingCourseDBO> findByCourse_CourseIdAndStudent_UserId(Long courseCourseId, Long studentUserId);

    List<RatingCourseDBO> findByCourse_CourseIdAndStudent_UserId(Long courseCourseId, Long studentUserId, Limit limit);
}
