package de.thu.thutorium.api.transferObjects.authentication;

import de.thu.thutorium.database.dbObjects.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Transfer object for the user registration request. Contains first name, last name, email,
 * password and role.
 */
public record RegisterRequestTO(
    @Email(message = "Email should be valid") @NotEmpty(message = "Email cannot be empty")
        String email,
    @NotNull(message = "Role cannot be empty") Role role,
    @NotEmpty(message = "First name cannot be empty") String firstName,
    @NotEmpty(message = "Last name cannot be empty") String lastName,
    @NotEmpty(message = "Password cannot be empty") String password) { }
