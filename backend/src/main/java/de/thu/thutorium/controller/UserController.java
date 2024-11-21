package de.thu.thutorium.controller;

import de.thu.thutorium.model.User;
import de.thu.thutorium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user-related endpoints.
 *
 * <p>This controller provides endpoints to get counts of different user roles, such as students and
 * tutors.
 */
@RestController
public class UserController {

  private final UserService userService;

  /**
   * Constructs a new {@code UserController} with the specified {@code UserService}.
   *
   * @param userService the service used to access user data
   */
  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Endpoint to get the total count of students.
   *
   * @return the total number of users with the role of 'student'
   * @apiNote This endpoint can be accessed via a GET request to '/students/count'.
   * @example GET /students/count
   * @response 42
   */
  @GetMapping("students/count")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public Long getStudentCount() {
    return userService.getStudentCount();
  }

  /**
   * Endpoint to get the total count of tutors.
   *
   * @return the total number of users with the role of 'tutor'
   * @apiNote This endpoint can be accessed via a GET request to '/tutors/count'.
   * @example GET /tutors/count
   * @response 15
   */
  @GetMapping("tutors/count")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public Long getTutorsCount() {
    return userService.getTutorCount();
  }

  /**
   * Retrieves the account details of a user based on their user ID.
   *
   * @param userId the unique identifier of the user, extracted from the query parameter
   * @return the {@link User} object containing the account details
   * @throws IllegalArgumentException if {@code userId} is null
   * @see UserService#findByUserId(Long)
   */
  @GetMapping("account")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public User getAccount(@RequestParam Long userId) {
    return userService.findByUserId(userId);
  }

  /**
   * Updates the details of an existing user based on their user ID.
   *
   * <p>The method accepts the updated user details in the request body and the user ID as a query
   * parameter. It calls the service layer to perform the update and returns an appropriate HTTP
   * response.
   *
   * @param userId the unique identifier of the user to be updated, extracted from the query
   *     parameter
   * @param updatedUser the {@link User} object containing the updated details, extracted from the
   *     request body
   * @return a {@link ResponseEntity} containing the updated {@link User} object with an HTTP 200
   *     status if the update is successful, or an HTTP 404 status with {@code null} if the user is
   *     not found
   * @throws IllegalArgumentException if {@code userId} or {@code updatedUser} is null
   * @see UserService#updateUser(Long, User)
   */
  @PutMapping("account")
  public ResponseEntity<User> updateUser(
      @RequestParam Long userId, // Extract userId from query parameter
      @RequestBody User updatedUser) {

    // Call the service to update the user based on the userId
    try {
      User updated = userService.updateUser(userId, updatedUser);
      return ResponseEntity.ok(updated); // Return the updated user with HTTP 200 status
    } catch (RuntimeException e) {
      return ResponseEntity.status(404).body(null); // Return 404 if user not found
    }
  }
}
