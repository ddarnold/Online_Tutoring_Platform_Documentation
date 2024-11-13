package de.thu.thutorium.repository;

import de.thu.thutorium.model.User;
import de.thu.thutorium.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
   * @example countByRole(UserRole.STUDENT) // returns 42
   */
  @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
  Long countByRole(UserRole role);
}
