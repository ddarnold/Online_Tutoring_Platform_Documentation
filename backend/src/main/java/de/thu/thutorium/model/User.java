package de.thu.thutorium.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user account entity within the system. This entity is mapped to the "user_account"
 * table in the database.
 *
 * <p>Includes basic user information such as first name, last name, role, verification status, and
 * the account creation timestamp.
 *
 * <p>Lombok annotations are used to automatically generate boilerplate code like getters, setters,
 * and constructors.
 */
@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  /**
   * The unique identifier for the user. It is generated automatically using an identity generation
   * strategy.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  /** The first name of the user. This field is required and cannot be null. */
  @Column(name = "first_name", nullable = false)
  private String firstName;

  /** The last name of the user. This field is required and cannot be null. */
  @Column(name = "last_name", nullable = false)
  private String lastName;

  /**
   * The role assigned to the user, represented as an enum. It is stored as a string in the
   * database.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private UserRole role;

  /**
   * Indicates whether the user's account has been verified. This field is required and has a
   * default value of false.
   */
  @Column(name = "is_verified", nullable = false)
  private Boolean isVerified = false;

  /**
   * The timestamp indicating when the user account was created. This field is required and cannot
   * be null.
   */
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
