package de.thu.thutorium.service;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.model.User;
import de.thu.thutorium.repository.CourseRepository;
import de.thu.thutorium.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling search operations related to tutors and courses.
 *
 * <p>This service provides methods to search for tutors by their full name and courses by their
 * name. It interacts with the {@link UserRepository} and {@link CourseRepository} to perform the
 * necessary database queries.
 */
@Service
public class SearchService {

  @Autowired private UserRepository userRepository;

  @Autowired private CourseRepository courseRepository;

  /**
   * Searches for tutors based on their full name or a partial match of their name.
   *
   * <p>Uses the {@link UserRepository#findByTutorFullName(String)} method to retrieve all users
   * with the role of "TUTOR" whose full name matches the provided search string. The search is
   * case-insensitive and supports partial matches.
   *
   * @param tutorName The search string to match against the tutor's full name. This parameter is
   *     compared case-insensitively and supports partial matching.
   * @return A list of {@link User} objects representing tutors whose full names match the search
   *     string.
   */
  public List<User> searchTutors(String tutorName) {
    return userRepository.findByTutorFullName(tutorName);
  }

  /**
   * Searches for courses by their name or partial match of the course name.
   *
   * <p>Uses the {@link CourseRepository#findCourseByName(String)} method to retrieve all courses
   * whose name matches the provided search string. The search is case-insensitive and supports
   * partial matches.
   *
   * @param courseName The search string to match against the course's name. This parameter is
   *     compared case-insensitively and supports partial matching.
   * @return A list of {@link Course} objects representing courses whose names match the search
   *     string.
   */
  public List<Course> searchCourses(String courseName) {
    return courseRepository.findCourseByName(courseName);
  }
}
