package de.thu.thutorium.database.dbObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a rating given by a student for a course.
 *
 * <p>This entity stores the details of the rating, including the student who gave the rating, the
 * course being rated, the rating points (e.g., 1.0 to 5.0), and an optional review text.
 *
 * @see UserDBO
 * @see CourseDBO
 */
@Entity
@Table(name = "rating_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCourseDBO {
  /** Primary key of the Rating table, automatically generated. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rating_id")
  @Setter(AccessLevel.NONE)
  private Long ratingId;

  /**
   * The student who gave the rating.
   *
   * <p>Defines a many-to-one relationship with {@link UserDBO}. The counterpart is denoted by
   * Set<RatingCourseDBO> 'givenCourseRatings' in {@link UserDBO}.
   */
  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private UserDBO student;

  /**
   * The course which received the rating.
   *
   * <p>Defines a many-to-one relationship with {@link CourseDBO}.The counterpart is denoted by
   * Set<RatingCourseDBO> 'receivedCourseRatings' in {@link CourseDBO}.
   */
  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private CourseDBO course;

  /** The rating points given by the student. */
  @Column(name = "points", nullable = false)
  @Min(0)
  @Max(10)
  private Double points;

  /** The review text provided by the student. */
  @Column(name = "review", columnDefinition = "TEXT")
  private String review;

  /** The timestamp when the rating was created. */
  @Column(name = "created_at")
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  public RatingCourseDBO(UserDBO student, CourseDBO course) {
    this.student = student;
    this.course = course;
    this.createdAt = LocalDateTime.now();
  }
}
