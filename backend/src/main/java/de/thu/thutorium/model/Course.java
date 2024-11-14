package de.thu.thutorium.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a course in the system. This entity is mapped to the "course" table in the database.
 * It contains details about the course such as its name, description, associated tutor, and
 * duration.
 */
@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

  /**
   * The unique identifier for the course. This value is auto-generated. The course ID is used as
   * the primary key in the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Long courseId;

  /**
   * The tutor responsible for the course. This is a many-to-one relationship with the {@link User}
   * entity. It cannot be null.
   *
   * @see User
   */
  @ManyToOne
  @JoinColumn(name = "tutor_id", nullable = false)
  private User tutor;

  /**
   * The name of the course. This value is required and cannot be null. It is used to identify and
   * describe the course in the system.
   */
  @Column(name = "course_name", nullable = false)
  private String courseName;

  /**
   * A brief description of the course. This provides additional information about the course
   * content. It can be null if not provided.
   */
  @Column(name = "description")
  private String description;

  /**
   * A category of the course. This field helps classify the course into a particular subject or
   * area. It is required and cannot be null.
   *
   * @see CourseCategory
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private CourseCategory category;

  /**
   * The timestamp when the course was created. This is typically set when the course record is
   * first saved to the database. It is automatically generated upon course creation.
   */
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  /**
   * The date when the course is scheduled to start. This value is required and cannot be null. It
   * represents the beginning date of the course.
   */
  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  /**
   * The date when the course is scheduled to end. This value is required and cannot be null. It
   * represents the ending date of the course.
   */
  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  // Uncomment the following code if you want to include a rating system for courses
  /**
   * The rating associated with the course. This is a many-to-one relationship with the {@link
   * Rating} entity. Uncomment if ratings are required.
   *
   * @see Rating
   */
  //  @ManyToOne
  //  @JoinColumn(name = "rating_id", nullable = false)
  //  private Rating rating;
}
