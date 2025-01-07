package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object (DTO) representing a rating given to a course by a student.
 *
 * <p>This class is used to transfer information related to a course's rating, including the rating
 * points, review text, the timestamp when the rating was created, and basic information about the
 * student who provided the rating.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingCourseTO {
  /** The unique identifier for the rating. */
  private Long ratingId;

  /** Basic information about the student who gave the rating. */
  @NotNull(message = "The student ID cannot be empty")
  private Long studentId;

  private String studentName;

  /** Basic information about the Course which received the rating who gave the rating. */
  @NotNull(message = "The course ID cannot be empty")
  private Long courseId;

  private String courseName;

  /**
   * The rating points given to the course.
   *
   * <p>This field stores the numeric value representing the rating points given by the student.
   * Typically, this might be a value between 1 and 5, depending on the rating system.
   */
  @NotNull(message = "The points cannot be null")
  private Double points;

  /**
   * The review text provided by the student.
   *
   * <p>This field contains the feedback or comments written by the student regarding the course. It
   * may include details such as the course content, teaching quality, and overall experience.
   */
  private String review;
}
