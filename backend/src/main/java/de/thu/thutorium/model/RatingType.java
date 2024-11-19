package de.thu.thutorium.model;

/**
 * Enum representing the type of rating within the system.
 *
 * <p>This enumeration is used to distinguish between ratings given to tutors and ratings given to
 * courses. It helps categorize and identify the context of a rating, ensuring that the associated
 * logic and data handling are applied correctly.
 *
 * <p>The two possible values are:
 *
 * <ul>
 *   <li>{@link #TUTOR} - Indicates that the rating is associated with a tutor.
 *   <li>{@link #COURSE} - Indicates that the rating is associated with a course.
 * </ul>
 */
public enum RatingType {
  /** Represents a rating given to a tutor. */
  TUTOR,

  /** Represents a rating given to a course. */
  COURSE
}
