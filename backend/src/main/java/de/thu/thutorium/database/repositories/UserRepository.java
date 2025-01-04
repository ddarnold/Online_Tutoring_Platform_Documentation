package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.RoleDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository interface for UserDBO entities. Provides methods for performing CRUD operations on
 * user data.
 */
@Repository
public interface UserRepository extends JpaRepository<UserDBO, Long> {
  /**
   * Finds a user entity by its username; in this case, email.
   *
   * @param email the email to search for
   * @return an Optional containing the found UserDBO, or empty if not found
   */
  Optional<UserDBO> findByEmail(String email);

//  /**
//   * Checks if a user entity exists with the given email.
//   *
//   * @param email the username to check
//   * @return true if a user entity exists with the given username, false otherwise
//   */
//  boolean existsByEmail(String email);

  /**
   * Finds a user by their email and roles.
   *
   * @param email the email of the user to find
   * @param roles the roles of the user to find
   * @return the user with the specified email and roles, or {@code null} if no such user exists
   */
  UserDBO findByEmailAndRoles(String email, Set<RoleDBO> roles);

  /**
   * Retrieves a {@link UserDBO} entity by its unique identifier.
   */
  Optional<UserDBO> findUserDBOByUserId(Long userId);


  /**
   * Retrieves a {@link UserDBO} entity with the role of "TUTOR" based on the specified user ID.
   *
   * @param userId the unique identifier of the tutor to be retrieved.
   * @return the {@link UserDBO} entity with the specified user ID and the role of "TUTOR", or
   *     {@code null} if no matching tutor is found.
   */
  @Query("SELECT u FROM UserDBO u JOIN u.roles r WHERE u.userId = :userId AND r.roleName = 'TUTOR'")
  UserDBO findByTutorId(@Param("userId") Long userId);

  /**
   * Finds tutors based on their full name or a partial match of their name.
   *
   * <p>This query retrieves all users with the role of "TUTOR" whose full name matches the given
   * search string, regardless of the order of the first and last name. The search is
   * case-insensitive and supports partial matches.
   *
   * @param tutorName The search string to match against the tutor's full name. This parameter is
   *     compared case-insensitively and supports partial matching.
   * @return A list of {@link UserDBO} objects representing tutors whose full names match the search
   *     string.
   * Todo: The method will match for any substring provided by the parameter, is this the expected behavior?
   */
  @Query(
      "SELECT u FROM UserDBO u JOIN u.roles r WHERE r.roleName = 'TUTOR' AND "
          + "(LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :tutorName, '%')) OR "
          + "LOWER(CONCAT(u.lastName, ' ', u.firstName)) LIKE LOWER(CONCAT('%', :tutorName, '%')))")
  List<UserDBO> findByTutorFullName(@Param("tutorName") String tutorName);

  /**
   * Finds a UserDBO entity based on the user's ID and role name.
   *
   * <p>
   * This method retrieves a user who has the specified user ID and role name. It
   * is useful for
   * checking if a user with a specific role exists.
   *
   * @param userId   The unique ID of the user.
   * @param roleName The role name to check for the user.
   * @return An {@link Optional} containing the {@link UserDBO} object if a user
   *         with the specified
   *         ID and role name exists, or an empty {@link Optional} if no such user
   *         is found.
   */
  Optional<UserDBO> findUserDBOByUserIdAndRoles_RoleName(Long userId, Role roleName);

}
