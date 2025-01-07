package de.thu.thutorium.api.transferObjects.common;

import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a tutor in the system.
 *
 * <p>This class extends the {@code UserBaseDTO} and adds specific fields relevant to a tutor, such
 * as a description of the tutor, the list of courses they teach, and their ratings.
 */
@Data
public class TutorTO extends UserTO {
  /**
   * A brief description or biography of the tutor.
   *
   * <p>This field holds information about the tutor, such as their background, teaching philosophy,
   * or areas of expertise. It is typically displayed in tutor profiles to help students learn more
   * about the tutor.
   */
  private String description;

  /**
   * The list of courses taught by the tutor.
   *
   * <p>This field holds a list of {@code CourseDTO} objects, each representing a course that the
   * tutor is teaching or has taught. This allows for easy retrieval of all the courses associated
   * with a tutor.
   */
  private List<CourseTO> tutorCourses;

  /**
   * The list of ratings given to the tutor by students.
   *
   * <p>This field contains a list of {@code RatingTutorDTO} objects, where each object represents a
   * rating or review provided by a student for the tutor. The ratings help provide insights into
   * the tutor's teaching effectiveness and overall student satisfaction.
   */
  private Double averageRating;
}
