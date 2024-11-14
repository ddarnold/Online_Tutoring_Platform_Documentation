package de.thu.thutorium.controller;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.service.CourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling HTTP requests related to courses. This controller
 * exposes endpoints to retrieve course data, such as fetching courses based on a tutor's name.
 */
@RestController
public class CourseController {

  @Autowired private CourseService courseService;

  /**
   * Retrieves a list of courses taught by a specific tutor based on the tutor's first and last
   * name. This endpoint takes the tutor's first name and last name as request parameters, queries
   * the service layer to find the matching courses, and returns the result.
   *
   * @param firstName the first name of the tutor to search for.
   * @param lastName the last name of the tutor to search for.
   * @return a list of {@link Course} objects taught by the specified tutor. If no matching courses
   *     are found, an empty list is returned.
   */
  @GetMapping("/courses/tutor")
  public List<Course> getCoursesByTutorName(
      @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
    return courseService.findCoursesByTutorName(firstName, lastName);
  }
}
