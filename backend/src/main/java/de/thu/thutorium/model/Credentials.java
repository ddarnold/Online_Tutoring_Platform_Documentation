package de.thu.thutorium.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a credentials entity within the system. This entity is mapped to the {@code
 * credentials} table in the database.
 *
 * <p>The credentials entity stores login information for a user, including the user's email, hashed
 * password, and an association with the {@link User} entity. It is the owning side of a one-to-one
 * relationship with the {@link User}.
 *
 * <p>Constraints include:
 *
 * <ul>
 *   <li>The {@code email} field must be unique and non-null.
 *   <li>The {@code hashedPassword} field must be non-null.
 *   <li>The {@link User} association must not be null, ensuring every credentials entry is linked
 *       to exactly one user.
 * </ul>
 *
 * <p>Lombok annotations are used to automatically generate boilerplate code such as getters,
 * setters, and constructors.
 *
 * @see User
 */
@Entity
@Table(name = "credentials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "credentials_id")
  private Long credentialsId;

  /** The user's email, used for login. This field must be unique. */
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  /** The hashed password for authentication. This field is mandatory. */
  @Column(name = "hashed_password", nullable = false)
  private String hashedPassword;

  /**
   * The user associated with these credentials. This is a one-to-one relationship and the owning
   * side.
   */
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;
}
