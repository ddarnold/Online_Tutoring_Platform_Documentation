package de.thu.thutorium.service;

import de.thu.thutorium.model.User;
import de.thu.thutorium.model.UserRole;
import de.thu.thutorium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
   * Updates the details of an existing user identified by their user ID.
   *
   * <p>This method fetches the user from the repository, updates their details with the data
   * provided in the {@code updatedUser} object, and saves the updated user back to the database.
   *
   * @param userId the unique identifier of the user to update
   * @param updatedUser the {@link User} object containing the updated information
   * @return the updated {@link User} object after saving to the database
   * @throws RuntimeException if the user with the specified {@code userId} is not found
   * @throws IllegalArgumentException if {@code userId} or {@code updatedUser} is null
   */
  public User updateUser(Long userId, User updatedUser) {
    // Find the existing user by userId
    Optional<User> existingUserOpt = userRepository.findById(userId);

    if (existingUserOpt.isPresent()) {
      User existingUser = existingUserOpt.get();

      // Update the existing user fields (skip userId since it's unchanged)
      existingUser.setFirstName(updatedUser.getFirstName());
      existingUser.setLastName(updatedUser.getLastName());
      existingUser.setRole(updatedUser.getRole());
      existingUser.setIsVerified(updatedUser.getIsVerified());
      existingUser.setCreatedAt(updatedUser.getCreatedAt());

      // Save the updated user back to the database
      return userRepository.save(existingUser);
    } else {
      // If the user with the specified userId is not found, throw an exception or handle it
      throw new RuntimeException("User not found with id: " + userId);
    }
  }
}
