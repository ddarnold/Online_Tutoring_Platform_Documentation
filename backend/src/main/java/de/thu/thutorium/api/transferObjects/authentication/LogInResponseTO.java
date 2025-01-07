package de.thu.thutorium.api.transferObjects.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transfer object for authentication user response. Contains the JWT token, creation time, and
 * expiration time.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponseTO {
  /** The JWT token. */
  private String token;

  /** The creation time of the token. */
  private String createdAt;

  /** The expiration time of the token. */
  private String expiresAt;

  /**
   * Constructs an AuthenticationUserResponseTO with the specified token, creation time, and
   * expiration duration.
   *
   * @param token the JWT token
   * @param createdAt the creation time of the token
   * @param expiresIn the duration in seconds until the token expires
   */
  public LogInResponseTO(String token, LocalDateTime createdAt, long expiresIn) {
    this.token = token;
    this.createdAt = formatDateTime(createdAt);
    this.expiresAt = formatDateTime(createdAt.plusSeconds(expiresIn / 1000));
  }

  public LogInResponseTO(String token) {
    this.token = token;
  }

  /**
   * Formats a LocalDateTime object to a string with the pattern "yyyy-MM-dd HH:mm:ss".
   *
   * @param dateTime the LocalDateTime object to format
   * @return the formatted date-time string
   */
  private String formatDateTime(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateTime.format(formatter);
  }
}
