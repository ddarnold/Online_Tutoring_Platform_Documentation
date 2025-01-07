package de.thu.thutorium.api.transferObjects.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

/** Transfer object for Login Request Contains username (email in this context) and password */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class LogInRequestTO {

  @Email
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Password cannot be empty")
  private String password;
}
