package de.thu.thutorium.api.transferObjects;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a course in the system.
 *
 * <p>This class holds information about a course, including its name, description, start and end
 * dates, the user who created the course, the associated category, and any ratings received for the
 * course.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseTO {

  private Long courseId;

  /**
   * The name of the course.
   *
   * <p>This field holds the name or title of the course, providing a quick reference to the course
   * content.
   */
  @NotNull(message = "Course name cannot be empty.")
  private String courseName;

  /** The tutor who created the course. */
  @NotNull(message = "Tutor cannot be empty.")
  private Long tutorId;

  /**
   * A short description of the course.
   *
   * <p>This field provides a brief overview of the course, usually meant to give potential students
   * a quick understanding of what the course covers.
   */
  private String descriptionShort;

  /**
   * A detailed description of the course.
   *
   * <p>This field contains an in-depth description of the course, outlining the course content,
   * objectives, prerequisites, and other important details.
   */
  private String descriptionLong;

  /**
   * The start date of the course.
   *
   * <p>This field indicates when the course will begin, which is important for students to know
   * when the course is available to attend or when the course content will be accessible.
   */
  private LocalDate startDate;

  /**
   * The end date of the course.
   *
   * <p>This field indicates when the course will conclude, which helps students know when to expect
   * the course to be completed or when the final assessments will take place.
   */
  private LocalDate endDate;

  private Double averageRating;
}
