package de.thu.thutorium.controller;

import de.thu.thutorium.model.Course;
import de.thu.thutorium.model.CourseCategory;
import de.thu.thutorium.service.CourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller class responsible for handling HTTP requests related to courses. / */
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
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
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

  /**
   * Retrieves a list of courses based on a partial name match.
   *
   * @param name The partial name of the course to search for.
   * @return A list of {@link Course} objects that match the specified partial name.
   */
  @GetMapping("/courses/search")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public List<Course> getCoursesByName(@RequestParam("name") String name) {
    return courseService.findCoursesByName(name);
  }

  /**
   * Retrieves a list of all distinct course categories available in the system. This endpoint is
   * accessible via a GET request to "/courses/categories".
   *
   * <p>The method leverages the {@link CourseService} to fetch unique course categories from the
   * database. It is annotated with {@code @CrossOrigin} to allow cross-origin requests from the
   * specified origin, particularly useful for frontend applications running on a different server
   * (e.g., a React app running on localhost:3000).
   *
   * @return a {@link List} of {@link CourseCategory} enums representing all unique course
   *     categories present in the database.
   * @see CourseService#getAllDistinctCourseCategories()
   */
  @GetMapping("/courses/categories")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public List<CourseCategory> getAllDCategories() {
    return courseService.getAllDistinctCourseCategories();
  }
}
