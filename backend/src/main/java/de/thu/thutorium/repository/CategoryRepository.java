package de.thu.thutorium.repository;

import de.thu.thutorium.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on {@link Category} entities.
 *
 * <p>This interface extends {@link JpaRepository}, which provides various methods for interacting
 * with the database, such as saving, finding, deleting, and updating entities. Additionally, custom
 * query methods can be defined to suit specific requirements.
 *
 * <p>By using {@code @Repository}, Spring treats this interface as a Data Access Object (DAO),
 * enabling exception translation into Spring's DataAccessException hierarchy.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * @Autowired
 * private CategoryRepository categoryRepository;
 *
 * List<Category> categories = categoryRepository.findAllCategories();
 * }</pre>
 *
 * @see Category
 * @see JpaRepository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Retrieves all categories from the database.
   *
   * <p>This method uses a custom JPQL query to select all instances of the {@link Category} entity.
   * It returns a {@link List} of categories. If no categories are found, an empty list is returned.
   *
   * <p>Note that this method is an alternative to using {@link JpaRepository#findAll()}, allowing
   * for additional customization if needed in the future.
   *
   * @return a {@link List} of {@link Category} objects representing all categories in the database.
   */
  @Query("SELECT c FROM Category c")
  List<Category> findAllCategories();
}
