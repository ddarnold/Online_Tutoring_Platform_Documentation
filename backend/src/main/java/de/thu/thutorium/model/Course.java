package de.thu.thutorium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a course entity with an ID, title, description, and category. This entity is mapped to
 * the "course" table in the database.
 *
 * <p>Lombok annotations are used to generate boilerplate code like getters, setters, and
 * constructors.
 */
@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

  /** The unique identifier for the course. Generated automatically using a UUID strategy. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  /** The title of the course. */
  private String title;

  /** A brief description of the course. */
  private String description;

  /**
   * The category of the course, represented as an enum. It is stored as a string in the database.
   */
  @Enumerated(EnumType.STRING)
  private CourseCategory category;

  // Additional fields and methods can be added here.

}
