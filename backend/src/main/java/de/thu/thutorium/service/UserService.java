package de.thu.thutorium.service;

import de.thu.thutorium.model.UserRole;
import de.thu.thutorium.model.User;
import de.thu.thutorium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing user-related operations.
 *
 * <p>This service provides methods to fetch counts of users based on their roles, such as students
 * and tutors.
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  /**
   * Constructs a new {@code UserService} with the specified {@code UserRepository}.
   *
   * @param userRepository the repository used to access user data
   */
  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Gets the total count of users with the role of 'STUDENT'.
   *
   * @return the total number of students in the system
   * @apiNote This method uses the {@link UserRepository#countByRole(UserRole)} method.
   * @example getStudentCount() // returns 42
   */
  public Long getStudentCount() {
    return userRepository.countByRole(UserRole.STUDENT);
  }

  /**
   * Gets the total count of users with the role of 'TUTOR'.
   *
   * @return the total number of tutors in the system
   * @apiNote This method uses the {@link UserRepository#countByRole(UserRole)} method.
   * @example getTutorCount() // returns 15
   */
  public Long getTutorCount() {
    return userRepository.countByRole(UserRole.TUTOR);
  }

  /**
   * Finds and retrieves a user by their unique user ID.
   *
   * @param userId the unique identifier of the user to retrieve
   * @return the {@link User} object if found
   * @throws IllegalArgumentException if {@code userId} is null
   */
  public User findByUserId(Long userId) {
    return userRepository.findByUserId(userId);
  }

  /**
   * Retrieves a {@link User} entity representing a tutor by their unique identifier.
   *
   * <p>This method fetches the {@link User} entity associated with the provided tutor ID by
   * delegating to the {@code findByTutorId} repository method. If no tutor exists with the
   * specified ID, the repository method returns {@code null}.
   *
   * @param tutorId the unique identifier of the tutor to be retrieved.
   * @return the {@link User} entity corresponding to the specified tutor ID, or {@code null} if no
   *     such tutor is found.
   */
  public User getTutorByID(Long tutorId) {
    return userRepository.findByTutorId(tutorId);
  }
}
