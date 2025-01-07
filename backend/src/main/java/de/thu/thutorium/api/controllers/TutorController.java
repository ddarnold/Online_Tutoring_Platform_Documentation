package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.api.transferObjects.common.ProgressTO;
import de.thu.thutorium.services.interfaces.CourseService;
import de.thu.thutorium.services.interfaces.MeetingService;
import de.thu.thutorium.services.interfaces.ProgressService;
import de.thu.thutorium.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * TutorController provides REST API endpoints for managing tutor-related operations such as
 * creating and managing meetings, courses, and progress records. It also includes functionality for
 * retrieving tutor information and deleting user accounts.
 *
 * <p>This controller handles requests under the "/tutor" path.
 *
 * <p>Annotations used:
 *
 * <ul>
 *   <li>@RestController: Marks this class as a REST controller.
 *   <li>@RequestMapping("/tutor"): Maps all requests starting with "/tutor".
 *   <li>@RequiredArgsConstructor: Generates a constructor for final fields.
 *   <li>@CrossOrigin: Enables cross-origin requests for specified endpoints.
 * </ul>
 */
@RestController
@RequestMapping("/tutor")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TutorController {
  /** Service for managing meeting-related operations. */
  private final MeetingService meetingService;

  /** Service for managing course-related operations. */
  private final CourseService courseService;

  /** Service for managing user-related operations. */
  private final UserService userService;

  /** Service for managing progress-related operations. */
  private final ProgressService progressService;

  /**
   * Creates a new meeting.
   *
   * @param meetingTO the {@link MeetingTO} object containing meeting details.
   * @return a success message.
   */
  @Operation(
      summary = "Create a new meeting",
      description = "Creates a new meeting for a tutor and course based on the provided details.",
      tags = {"Meeting Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Meeting created successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MeetingTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Tutor/Course/Address not found",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  @PostMapping("/create-meeting")
  public ResponseEntity<?> createMeeting(@RequestBody @Valid MeetingTO meetingTO) {
    try {
      MeetingTO meeting = meetingService.createMeeting(meetingTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(meeting);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Deletes a meeting by its ID.
   *
   * @param meetingId the ID of the meeting to delete.
   * @return a success message.
   */
  @Operation(
      summary = "Delete a meeting",
      description = "Deletes an existing meeting by its ID.",
      tags = {"Meeting Endpoints"})
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Meeting deleted successfully"),
    @ApiResponse(
        responseCode = "404",
        description = "Meeting not found",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  @DeleteMapping("/delete-meeting/{meetingId}")
  public ResponseEntity<?> deleteMeeting(@PathVariable Long meetingId) {
    try {
      meetingService.deleteMeeting(meetingId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Meeting deleted successfully");
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Updates a meeting by its ID.
   *
   * @param meetingId the ID of the meeting to update.
   * @param meetingTO the {@link MeetingTO} object containing updated meeting details.
   * @return a success message.
   */
  @Operation(
      summary = "Update a meeting",
      description = "Updates the details of an existing meeting by its ID.",
      tags = {"Meeting Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Meeting updated successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MeetingTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Meeting not found",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  @PutMapping("/update-meeting/{meetingId}")
  public ResponseEntity<?> updateMeeting(
      @PathVariable Long meetingId, @RequestBody @Valid MeetingTO meetingTO) {
    try {
      MeetingTO updatedMeeting = meetingService.updateMeeting(meetingId, meetingTO);
      return ResponseEntity.status(HttpStatus.OK).body(updatedMeeting);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /** Course Operations */

  /**
   * Creates a new course.
   *
   * @param courseTO the {@link CourseTO} object containing course details.
   * @return a success message.
   */
  @Operation(
      summary = "Create a new course",
      description = "Creates a new course with the specified details.",
      tags = {"Course Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Course created successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CourseTO.class))),
    @ApiResponse(
        responseCode = "409",
        description = "Entity already exists in the database",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  @PostMapping("/course/create")
  public ResponseEntity<?> createCourse(@RequestBody @Valid CourseTO courseTO) {
    try {
      CourseTO course = courseService.createCourse(courseTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(course);
    } catch (EntityExistsException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Deletes a course by its ID.
   *
   * @param courseId the ID of the course to delete.
   * @return a success message.
   */
  @Operation(
      summary = "Delete a course by ID",
      description = "Deletes an existing course by its unique ID.",
      tags = {"Course Endpoints"})
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Course not found")
  })
  @DeleteMapping("/delete-course/{courseId}")
  public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
    try {
      courseService.deleteCourse(courseId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course deleted successfully");
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Updates a course by its ID.
   *
   * @param courseId the ID of the course to update.
   * @param courseTO the {@link CourseTO} object containing updated course details.
   * @return a success message.
   */
  @Operation(
      summary = "Update a course by ID",
      description = "Updates the details of an existing course by its unique ID.",
      tags = {"Course Endpoints"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Course updated successfully",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CourseTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Course not found",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = String.class)))
  })
  @PutMapping("/update-course/{courseId}")
  public ResponseEntity<?> updateCourse(
      @PathVariable Long courseId, @RequestBody @Valid CourseTO courseTO) {
    try {
      CourseTO updatedCourse = courseService.updateCourse(courseId, courseTO);
      return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
    } catch (EntityNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /** Progress Operations */

  /**
   * Creates a new progress record.
   *
   * @param progressTO the {@link ProgressTO} object containing progress details.
   * @return a success message.
   */
  @Operation(
      summary = "Create progress record",
      description = "Creates a new progress record for a student in a specific course.",
      tags = {"Progress Endpoints"})
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Progress created successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data")
  })
  @PostMapping("/create-progress")
  public ResponseEntity<String> createProgress(@Valid @RequestBody ProgressTO progressTO) {
    progressService.createProgress(progressTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("Progress created successfully");
  }

  /**
   * Deletes a progress record by student ID and course ID.
   *
   * @param studentId the ID of the student.
   * @param courseId the ID of the course.
   * @return a success message or a not-found message.
   */
  @Operation(
      summary = "Delete progress record",
      description = "Deletes a student's progress record for a specific course.",
      tags = {"Progress Endpoints"})
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Progress deleted successfully"),
    @ApiResponse(responseCode = "404", description = "Progress record not found")
  })
  @DeleteMapping("/delete-progress/{studentId}/{courseId}")
  public ResponseEntity<String> deleteProgress(
      @PathVariable Long studentId, @PathVariable Long courseId) {
    boolean isDeleted = progressService.deleteProgress(studentId, courseId);
    if (isDeleted) {
      return ResponseEntity.ok("Progress deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Progress record not found");
    }
  }

  /**
   * Updates a progress record by student ID and course ID.
   *
   * @param studentId the ID of the student.
   * @param courseId the ID of the course.
   * @param points the new progress points.
   * @return a success message or a not-found message.
   */
  @Operation(
      summary = "Update progress record",
      description = "Updates the progress points for a student in a specific course.",
      tags = {"Progress Endpoints"})
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Progress updated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid input data"),
    @ApiResponse(responseCode = "404", description = "Progress record not found")
  })
  @PutMapping("/update-progress/{studentId}/{courseId}")
  public ResponseEntity<String> updateProgress(
      @PathVariable Long studentId, @PathVariable Long courseId, @RequestParam Double points) {

    boolean isUpdated = progressService.updateProgress(studentId, courseId, points);
    if (isUpdated) {
      return ResponseEntity.ok("Progress updated successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Progress record not found");
    }
  }
}
