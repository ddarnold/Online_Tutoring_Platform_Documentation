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

  /** Primary key of the Rating table, automatically generated. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rating_id")
  private Long ratingId;

  /**
   * The user who is receiving the rating. Can be a tutor or course instructor. Mapped as a foreign
   * key to the User table.
   */
  @ManyToOne
  @JoinColumn(name = "rated_user_id")
  @JsonIgnoreProperties({"ratings", "credentials", "courses"})
  private User ratedUser;

  /**
   * The user who created the rating, typically a student. Mapped as a foreign key to the User
   * table.
   */
  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  @JsonIgnoreProperties({"ratings", "credentials", "courses"})
  private User student;

  /**
   * The course associated with the rating. This field is optional. Mapped as a foreign key to the
   * Course table.
   */
  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  /** The numerical rating points, in the range of 1 to 5. */
  @Column(name = "points", nullable = false)
  private Integer points;

  /** Optional review text provided by the student. Maximum length is 1000 characters. */
  @Column(name = "review", length = 1000)
  private String review;

  /**
   * The type of the rating, indicating whether it applies to a tutor or a course. Stored as an
   * enumerated string.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "rating_type", nullable = false)
  private RatingType ratingType;
}
