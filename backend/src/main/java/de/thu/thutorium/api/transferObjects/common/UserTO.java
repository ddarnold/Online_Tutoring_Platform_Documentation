package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) that represents the base information of a user in the system.
 *
 * <p>This class is used to transfer common user-related data between layers of the application. It
 * contains fields that are shared among all types of users, such as students and tutors. Specific
 * user types may extend or customize this DTO for additional fields.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTO {
  /**
   * The unique identifier of the user.
   *
   * <p>This field holds the ID of the user, which is typically used to identify the user in the
   * system.
   */
  private Long userId;

  /**
   * The first name of the user.
   *
   * <p>This field stores the first name of the user, which is a part of their personal information.
   */
  @NotEmpty(message = "The firstname cannot be empty")
  private String firstName;

  /**
   * The last name of the user.
   *
   * <p>This field stores the last name of the user, which is another part of their personal
   * information.
   */
  @NotEmpty(message = "The last name cannot be empty")
  private String lastName;

  /** The full name of the user, which is a combination of the first name and last name. */
  private String fullName;

  /**
   * The email address of the user.
   *
   * <p>This field contains the email address of the user, which is used for communication purposes
   * and may be used as a unique identifier.
   */
  @Email(message = "Email should be valid")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  /** The affiliation of the user. */
  private AffiliationTO affiliation;

  private String description;
}
