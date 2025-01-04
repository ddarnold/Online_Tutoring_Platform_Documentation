package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.exceptions.ResourceNotFoundException;
import de.thu.thutorium.services.interfaces.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Controller class responsible for handling HTTP requests related to courses. */
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

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
                          schema = @Schema(implementation = CourseTO.class)
                  )
          ),
          @ApiResponse(
                  responseCode = "404",
                  description = "Course does not exist in database",
                  content =
                  @Content(
                          mediaType = "application/json",
                          schema = @Schema(implementation = String.class)
                  )
          )
  })
  @GetMapping("/get-course")
  public ResponseEntity<?> getCourseById(@Parameter(
          name = "course ID",
          description = "The ID of the course to be queried",
          required = true
  ) @RequestParam Long id) {
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
