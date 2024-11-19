package de.thu.thutorium.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a rating given by a student, which can be for a tutor or a course.
 *
 * <p>This entity stores the details of a rating, including the student who gave the rating, the
 * tutor or course being rated, the rating points (e.g., 1.0 to 5.0), and an optional review text.
 *
 * <p>A rating can be associated with a tutor or a course, and it is stored with information about
 * the student who created the rating.
 *
 * <p>The entity supports cascade operations to manage its relationships, such as the deletion of
 * associated entities (like `User` or `Course`).
 *
 * @see User
 * @see Course
 * @see RatingType
 */
@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

  /** The unique identifier for the rating. This is the primary key of the rating entity. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rating_id")
  private Long ratingId;

  /**
   * The student who gave the rating. This relationship is mapped by the {@code student_id} field in
   * the {@link Rating} entity. The student's ratings as both a tutor and a student are ignored
   * during serialization to avoid circular references.
   *
   * @see User
   */
  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  @JsonIgnoreProperties({"ratingsAsTutor", "ratingsAsStudent", "courses", "credentials"})
  private User student; // The student creating the rating

  /**
   * The tutor being rated, if applicable. This is an optional field and will only be populated if
   * the rating is for a tutor. The tutor's ratings as a student, tutor, their courses, and
   * credentials are ignored during serialization to avoid circular references.
   *
   * @see User
   */
  @ManyToOne
  @JoinColumn(name = "tutor_id")
  @JsonIgnoreProperties({"ratingsAsTutor", "ratingsAsStudent", "courses", "credentials"})
  private User tutor;

  /**
   * The course being rated, if applicable. This is an optional field and will only be populated if
   * the rating is for a course.
   *
   * @see Course
   */
  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  /**
   * The rating value given by the student. This is a required field with a numerical value
   * representing the rating score, ranging from 1.0 to 5.0
   */
  @Column(name = "points", nullable = false)
  private Integer points;

  /**
   * An optional textual review given by the student along with the rating points. The length of the
   * review is capped at 1000 characters.
   */
  @Column(name = "review", length = 1000)
  private String review; // Optional review text

  /**
   * The type of rating (e.g., for a tutor or a course). This is an enum value that distinguishes
   * the rating target (either tutor or course).
   *
   * @see RatingType
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "rating_type", nullable = false)
  private RatingType ratingType; // Enum to identify the rating target
}
