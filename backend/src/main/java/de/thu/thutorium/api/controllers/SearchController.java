package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.TutorTO;
import de.thu.thutorium.exceptions.ResourceNotFoundException;
import de.thu.thutorium.services.interfaces.CategoryService;
import de.thu.thutorium.services.interfaces.CourseService;
import de.thu.thutorium.services.interfaces.SearchService;
import de.thu.thutorium.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing search functionalities for tutors, courses, and general information.
 *
 * <p>This controller provides endpoints that allow users to:
 *
 * <ul>
 *   <li>Search for tutors by name
 *   <li>Search for courses by name
 *   <li>Retrieve a list of course categories
 *   <li>Get a list of courses by category
 *   <li>Retrieve the total count of students, tutors, and courses on the platform
 *   <li>etc.
 * </ul>
 *
 * <p><b>Access:</b> This controller is publicly accessible, meaning it can be accessed without
 * authentication. It is designed to provide search functionality and general platform information
 * to both logged-in and not logged-in users.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;
  private final CategoryService categoryService;
  private final CourseService courseService;
  private final UserService userService;

  /**
   * Searches for tutors or courses based on the provided query parameters.
   *
   * <p>This endpoint supports searching for tutors by name and courses by name. If both parameters
   * are provided, the search will return combined results of matching tutors and courses.
   *
   * @param tutorName Optional. The name of the tutor to search for. If null or empty, tutor search
   *     is skipped.
   * @param courseName Optional. The name of the course to search for. If null or empty, course
   *     search is skipped.
   * @return A list of search results containing either tutors (UserBaseDTO), courses (CourseDTO),
   *     or both, depending on the provided parameters. Results may include duplicates if multiple
   *     entities match the search criteria.
   * @throws jakarta.persistence.EntityNotFoundException if the searched parameters are not found.
   *     Todo: Review: - The method will find match for any course and tutor names, even if the
   *     courses are not being offered by the respective tutors; is this by design? - Passing of
   *     special characters 'C++' seem to cause internal errors?
   */
  @Operation(
      summary = "Search tutors or courses",
      description = "Search for tutors by name or courses by name. ",
      tags = {"Search Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Search results returned successfully",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = Object.class))))
  })
  @GetMapping
  public ResponseEntity<?> search(
      @Parameter(
              name = "tutorName",
              description =
                  "The name of the tutor to be found. The method returns results for"
                      + "any partial match of the name passed. This parameter is optional and the method returns "
                      + "only the courses searched for if this parameter is empty.",
              required = false)
          @RequestParam(required = false)
          String tutorName,
      @Parameter(
              name = "courseName",
              description =
                  "The name of the course to be found. The method returns results for"
                      + "any partial match of the course name passed.  This parameter is optional"
                      + " and the method returns only the tutors searched for if this parameter is empty.",
              required = false)
          @RequestParam(required = false)
          String courseName) {
    try {
      // Initialize an empty list to store results
      List<Object> results = new ArrayList<>();

      // If tutorName is provided, search for tutors and add to the results
      if (tutorName != null && !tutorName.isEmpty()) {
        List<TutorTO> tutors = searchService.searchTutors(tutorName);
        results.addAll(tutors); // Add tutors to the results list
      }

      // If courseName is provided, search for courses and add to the results
      if (courseName != null && !courseName.isEmpty()) {
        List<CourseTO> courses = courseService.searchCourses(courseName);
        results.addAll(courses); // Add courses to the results list
      }
      // Return the combined results without removing duplicates
      return ResponseEntity.status(HttpStatus.OK).body(results);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Gets all course categories.
   *
   * <p>This endpoint fetches all the available course categories from the database.
   *
   * @return {@code List} of all the available {@link CourseCategoryTO} objects.
   */
  @Operation(
      summary = "Retrieve all course categories",
      description = "Fetches a list of all available course categories in the system.",
      tags = {"Search Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Categories retrieved successfully",
        content =
            @Content(
                array = @ArraySchema(schema = @Schema(implementation = CourseCategoryTO.class)))),
  })
  @GetMapping("/categories")
  public ResponseEntity<?> getCategories() {
    try {
      List<CourseCategoryTO> categories = categoryService.getAllCategories();
      return ResponseEntity.status(HttpStatus.OK).body(categories);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Retrieves a list of courses based on the specified category name.
   *
   * @param categoryName The name of the category for which courses are to be retrieved.
   * @return A list of {@link CourseTO} objects that belong to the specified category. Todo: Is the
   *     method case-insensitive for parameters? Does it provide/ need to provide partial matches?
   *     Todo: Review the repository method? Todo: The url is misleading? Shouldn't it be
   *     course/Category name?
   */
  @Operation(
      summary = "Retrieve courses by category",
      description = "Fetches courses that belong to a specific category.",
      tags = {"Search Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Courses retrieved successfully",
        content =
            @Content(array = @ArraySchema(schema = @Schema(implementation = CourseTO.class)))),
  })
  @GetMapping("/category/{categoryName}")
  public ResponseEntity<?> getCoursesByCategory(@PathVariable String categoryName) {
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(courseService.getCoursesByCategory(categoryName));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Endpoint to get the total count of students.
   *
   * @return the total number of users with the role of 'student'
   */
  @Operation(
      summary = "Get total student count",
      description = "Retrieves the total number of students registered in the system.",
      tags = {"Search Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Student count retrieved successfully",
        content = @Content(schema = @Schema(implementation = Long.class))),
  })
  @GetMapping("students/count")
  public Long getStudentCount() {
    return userService.getStudentCount();
  }

  /**
   * Endpoint to get the total count of tutors.
   *
   * @return the total number of users with the role of 'tutor'
   */
  @Operation(
      summary = "Get total tutor count",
      description = "Retrieves the total number of tutors registered in the system.",
      tags = {"Search Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Tutor count retrieved successfully",
        content = @Content(schema = @Schema(implementation = Long.class))),
  })
  @GetMapping("tutors/count")
  public Long getTutorsCount() {
    return userService.getTutorCount();
  }

  /**
   * Handles a GET request to retrieve the total count of courses.
   *
   * @return the total number of courses as a {@code Long}.
   */
  @Operation(
      summary = "Get total course count",
      description = "Retrieves the total number of courses available in the system.",
      tags = {"Search Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Course count retrieved successfully",
        content = @Content(schema = @Schema(implementation = Long.class))),
  })
  @GetMapping("/courses/count")
  public Long getCoursesCount() {
    return courseService.getTotalCountOfCourses();
  }

  /**
   * Retrieves a course based on its ID.
   *
   * @param id The ID of the course to retrieve.
   * @return The {@link CourseTO} object representing the course.
   * @throws ResourceNotFoundException, if the searched course does not exist in the database.
   */
  @Operation(
      summary = "A user can search for and retrieve a course if it exists in the database",
      description =
          "Allows a user to search for the course by its user ID. If the course does not exist in the database"
              + "an appropriate error is thrown.",
      tags = {"Course Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "The course is retrieved successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CourseTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Course does not exist in database",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  @GetMapping("/get-course/{id}")
  public ResponseEntity<?> getCourseById(
      @Parameter(
              name = "course ID",
              description = "The ID of the course to be queried",
              required = true)
          @PathVariable
          Long id) {
    try {
      CourseTO course = courseService.findCourseById(id);
      return ResponseEntity.status(HttpStatus.OK).body(course);
    } catch (ResourceNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }
}
