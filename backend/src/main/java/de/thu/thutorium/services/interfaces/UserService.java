package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.RatingTutorTO;
import de.thu.thutorium.api.transferObjects.common.UserTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * The {@code UserService} interface provides methods for managing and retrieving user data.
 *
 * <p>It exposes the following functionalities:
 *
 * <ul>
 *   <li>Get the total count of users with the role of 'STUDENT'.
 *   <li>Get the total count of users with the role of 'TUTOR'.
 *   <li>Find and retrieve a user by their unique user ID.
 *   <li>Retrieve a tutor by their unique tutor ID.
 *   <li>Delete a user by their unique user ID.
 * </ul>
 */
public interface UserService {

  /**
   * Gets the total count of users with the role of 'STUDENT'.
   *
   * @return the total number of students in the system
   */
  Long getStudentCount();

  /**
   * Gets the total count of users with the role of 'TUTOR'.
   *
   * @return the total number of tutors in the system
   */
  Long getTutorCount();

  /**
   * Finds and retrieves a user by their unique user ID.
   *
   * @param userId the unique identifier of the user to retrieve
   * @return the {@link de.thu.thutorium.database.dbObjects.UserDBO} object if found
   * @throws IllegalArgumentException if {@code userId} is null
   */
  UserTO findByUserId(Long userId);

  /**
   * Retrieves a {@link de.thu.thutorium.database.dbObjects.UserDBO} entity representing a tutor by
   * their unique identifier.
   *
   * @param tutorId the unique identifier of the tutor to be retrieved
   * @return the {@link de.thu.thutorium.database.dbObjects.UserDBO} entity corresponding to the
   *     specified tutor ID, or {@code null} if no such tutor is found
   */
  UserTO getTutorByID(Long tutorId);

  /**
   * Deletes a user by their unique user ID.
   *
   * <p>This method removes the user corresponding to the provided user ID from the system.
   *
   * @param userId the unique identifier of the user to delete.
   * @throws IllegalArgumentException if {@code userId} is {@code null}.
   */
  void deleteUser(Long userId);

  /**
   * Updates an existing user.
   *
   * @param id the id of the user
   * @param user the {@code UserTO} object containing the user data
   * @return the created {@code UserTO} object
   */
  UserTO updateUser(Long id, UserTO user);

  /**
   * Enrolls a student in a course
   *
   * @param studentId the id of the student
   * @param courseId the id of the course
   */
  void enrollCourse(Long studentId, Long courseId);

  /**
   * User rates an existing tutor.
   *
   * @param ratingTutorTO the {@link RatingTutorTO} which contains details of the review.
   */
  void rateTutor(@Valid RatingTutorTO ratingTutorTO);

  /**
   * A student unenrolls from a course.
   *
   * @param studentId the id of the student who unenrolls.
   * @param courseId the id of the course from which the student unenrolls.
   */
  void unenrollCourse(Long studentId, Long courseId);

  List<RatingTutorTO> getTutorRatings(Long tutorId);

  List<CourseTO> getCoursesEnrolled(Long studentId);

}
