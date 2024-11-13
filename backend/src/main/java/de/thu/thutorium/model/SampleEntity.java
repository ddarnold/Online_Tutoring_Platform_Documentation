package de.thu.thutorium.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a sample entity that includes an ID, first name, and last name. This entity is mapped
 * to the "sample" table in the database.
 *
 * <p>This class uses Lombok annotations to automatically generate boilerplate code such as getters,
 * setters, and constructors.
 */
@Entity
@Table(name = "sample")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleEntity {

  /**
   * The unique identifier for this sample entity. It is generated automatically using an identity
   * generation strategy.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** The first name associated with this sample entity. */
  private String firstName;

  /** The last name associated with this sample entity. */
  private String lastName;
}
