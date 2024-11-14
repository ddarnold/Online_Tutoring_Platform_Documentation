package de.thu.thutorium.service;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.repository.CourseRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling operations related to courses. This class interacts with
 * the {@link CourseRepository} to fetch courses based on specific criteria such as tutor's name.
 */
@Service
public class CourseService {

  @Autowired private CourseRepository courseRepository;

  /**
   * Finds a list of courses taught by a tutor based on the tutor's first and last names. This
   * method queries the repository to retrieve all courses where the tutor's first and last name
   * match the provided parameters.
   *
   * @param firstName The tutor's first name.
   * @param lastName The tutor's last name.
   * @return A list of {@link Course} objects taught by the tutor with the provided name. If no
   *     courses are found, an empty list is returned.
   */
  public List<Course> findCoursesByTutorName(String firstName, String lastName) {
    return courseRepository.findByTutorFirstNameAndLastName(firstName, lastName);
  }
}
