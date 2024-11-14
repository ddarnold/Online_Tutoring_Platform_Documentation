package de.thu.thutorium.controller;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.service.CourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller class responsible for handling HTTP requests related to courses. */
@RestController
public class CourseController {

  @Autowired private CourseService courseService;

  /**
   * Retrieves a list of courses taught by a specific tutor. The search can be based on either the
   * tutor's full name or by first and last names.
   *
   * @param firstName the first name of the tutor (optional).
   * @param lastName the last name of the tutor (optional).
   * @param tutorName the full name of the tutor (optional).
   * @return a list of {@link Course} objects taught by the specified tutor.
   */
  @GetMapping("/courses/tutor")
  public List<Course> getCoursesByTutor(
      @RequestParam(required = false) String firstName,
      @RequestParam(required = false) String lastName,
      @RequestParam(required = false) String tutorName) {

    // If firstName and lastName are provided, search by both first and last names
    if (firstName != null && lastName != null) {
      return courseService.findCoursesByTutorName(firstName, lastName);
    }

    // If tutorName is provided, search by the full name
    if (tutorName != null) {
      return courseService.findCoursesByFullTutorName(tutorName);
    }

    // If no parameters are provided, return an empty list or an appropriate response
    return List.of();
  }
}
