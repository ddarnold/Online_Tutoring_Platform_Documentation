package de.thu.thutorium.controller;

import de.thu.thutorium.model.User;
import de.thu.thutorium.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
