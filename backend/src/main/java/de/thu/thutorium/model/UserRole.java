package de.thu.thutorium.model;

/**
 * Enum representing the different roles a user can have in the system.
 *
 * <p>Each role is associated with specific permissions and capabilities within the application.
 */
public enum UserRole {

  /** Represents an administrator with the highest level of access and control. */
  ADMIN,

  /** Represents a student with access to learning materials and course enrollment. */
  STUDENT,

  /** Represents a tutor who can create and manage educational content. */
  TUTOR
}
