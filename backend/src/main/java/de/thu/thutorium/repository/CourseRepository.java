package de.thu.thutorium.repository;

import de.thu.thutorium.model.Course;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link Course} entities.
 *
 * <p>This interface extends the {@link CrudRepository} interface provided by Spring Data, enabling
 * basic CRUD (Create, Read, Update, Delete) operations on Course entities.
 *
 * <p>By extending {@link CrudRepository}, this interface inherits several methods for working with
 * Course persistence, including methods for saving, deleting, and finding Course entities.
 *
 * @see Course
 * @see CrudRepository
 */
public interface CourseRepository extends CrudRepository<Course, Integer> {}
