package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.RatingCourseTO;
import org.springframework.stereotype.Service;

import java.util.List;

/** The {@code CourseService} interface provides methods for retrieving and searching courses. */
@Service
public interface CourseService {
  /**
   * Finds a course by its unique ID.
   *
   * <p>This method returns the {@link CourseTO} object corresponding to the given course ID. If no
   * course is found for the provided ID, it returns {@code null}.
   *
   * @param id the unique ID of the course to retrieve.
   * @return the {@link CourseTO} object representing the course with the specified ID, or {@code
   *     null} if no course with the given ID exists.
   */
  CourseTO findCourseById(Long id);

  /**
   * Retrieves courses that belong to a specific category.
   *
   * <p>This method returns a list of {@link CourseTO} objects that belong to the given category.
   *
   * @param categoryName the name of the category to search for.
   * @return a list of {@link CourseTO} objects representing courses in the specified category. If
   *     no courses are found for the given category, an empty list is returned.
   */
  List<CourseTO> getCoursesByCategory(String categoryName);

  List<CourseTO> getCourseByTutorId(Long tutorId);

  /**
   * Gets the total count of courses available.
   *
   * <p>This method returns the total number of courses available in the system.
   *
   * @return the total number of courses as a {@code Long} value.
   */
  Long getTotalCountOfCourses();

  /**
   * Creates a new course.
   *
   * <p>This method creates a new course in the system based on the provided {@link CourseTO}
   * object.
   *
   * @param courseTO the {@link CourseTO} object containing the data for the course to be created.
   * @return the {@link de.thu.thutorium.database.dbObjects.CourseDBO} object created.
   */
  CourseTO createCourse(CourseTO courseTO);

  /**
   * Deletes a course by its ID.
   *
   * <p>This method deletes the course with the specified ID from the system.
   *
   * @param courseId the unique ID of the course to be deleted.
   */
  void deleteCourse(Long courseId);

  /**
   * Updates the details of an existing course.
   *
   * <p>This method updates the course data based on the provided {@link CourseTO} object.
   *
   * @param courseId the unique ID of the course to be updated.
   * @param courseTO the {@link CourseTO} object containing the new course data.
   * @return
   */
  CourseTO updateCourse(Long courseId, CourseTO courseTO);

  /**
   * User rates an existing course.
   *
   * @param ratingCourseTO the {@link RatingCourseTO} which contains details of the review.
   */
  void rateCourse(RatingCourseTO ratingCourseTO);

  /**
   * Searches for courses by the given course name.
   *
   * <p>This method will return a list of {@link CourseTO} objects that match the given course name.
   * The search can support case-insensitivity and partial name matches depending on the
   * implementation.
   *
   * @param courseName the name (or partial name) of the course to search for.
   * @return a list of {@link CourseTO} objects representing courses that match the search criteria.
   *     If no courses are found, an empty list is returned.
   */
  List<CourseTO> searchCourses(String courseName);
}
