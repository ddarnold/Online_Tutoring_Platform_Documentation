package de.thu.thutorium.repository;

import de.thu.thutorium.model.User;
import de.thu.thutorium.model.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>This interface provides methods for interacting with the user data stored in the database,
 * including custom queries to count users based on their roles.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Counts the number of users with a specific role.
   *
   * @param role the {@link UserRole} to count (e.g., STUDENT, TUTOR)
   * @return the total count of users with the specified role
   * @apiNote This method uses a custom JPQL query to count users by their role.
   */
  @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
  Long countByRole(UserRole role);

  /**
   * Finds tutors based on their full name or a partial match of their name.
   *
   * <p>This query retrieves all users with the role of "TUTOR" whose full name matches the given
   * search string, regardless of the order of the first and last name. The search is
   * case-insensitive and supports partial matches.
   *
   * <p>For example, a search for "charlie brown" will match both "Charlie Brown" and "Brown
   * Charlie".
   *
   * @param tutorName The search string to match against the tutor's full name. This parameter is
   *     compared case-insensitively and supports partial matching.
   * @return A list of {@link User} objects representing tutors whose full names match the search
   *     string.
   */
  @Query(
      "SELECT u FROM User u WHERE u.role = 'TUTOR' AND (LOWER(CONCAT(u.firstName, ' ', u.lastName))"
          + " LIKE LOWER(CONCAT('%', :tutorName, '%')) OR LOWER(CONCAT(u.lastName, ' ',"
          + " u.firstName)) LIKE LOWER(CONCAT('%', :tutorName, '%')))")
  List<User> findByTutorFullName(@Param("tutorName") String tutorName);

  /**
   * Retrieves a {@link User} entity by its unique identifier.
   *
   * @param userId The unique identifier of the user to be retrieved.
   * @return The {@link User} entity with the specified user ID, or {@code null} if no user is
   *     found.
   */
  @Query("SELECT u FROM User u WHERE u.userId = :userId")
  User findByUserId(@Param("userId") Long userId);

  /**
   * Retrieves a {@link User} entity with the role of "TUTOR" based on the specified user ID.
   *
   * <p>This method executes a custom JPQL query to fetch a {@link User} whose unique identifier
   * matches the provided {@code userId} and whose role is explicitly set to "TUTOR". If no such
   * user exists, the method returns {@code null}.
   *
   * @param userId the unique identifier of the tutor to be retrieved.
   * @return the {@link User} entity with the specified user ID and the role of "TUTOR", or {@code
   *     null} if no matching tutor is found.
   */
  @Query("SELECT u FROM User u WHERE u.userId = :userId AND  u.role = 'TUTOR'")
  User findByTutorId(@Param("userId") Long userId);
}
