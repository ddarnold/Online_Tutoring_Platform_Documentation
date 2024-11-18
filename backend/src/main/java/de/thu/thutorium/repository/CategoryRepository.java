package de.thu.thutorium.repository;

import de.thu.thutorium.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Category entities.
 * <p>
 * By extending JpaRepository, it inherits methods for interacting with the database,
 * such as saving, finding, and deleting Category instances.
 * <p>
 * The custom query "findAllCategoriesWithCourses" fetches categories along with their
 * associated courses.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Custom JPQL query to retrieve all categories along with their associated courses.
     * This uses JOIN FETCH to eagerly load the course data when fetching categories.
     *
     * @return List of categories with associated courses.
     */
    @Query("SELECT c FROM Category c JOIN FETCH c.courses")
    List<Category> findAllCategoriesWithCourses();
}
