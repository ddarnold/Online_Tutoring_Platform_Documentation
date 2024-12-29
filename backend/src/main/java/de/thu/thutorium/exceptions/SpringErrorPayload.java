package de.thu.thutorium.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Represents the error response payload for invalid requests.
 *
 * <p>The {@link SpringErrorPayload} class defines the structure of error responses, which is used
 * in the {@link de.thu.thutorium.swagger.CommonApiResponses} annotation to describe the schema of
 * error responses in the Swagger documentation. The {@link GlobalExceptionHandler} class uses the
 * {@link SpringErrorPayload} class to structure the error responses returned to the client when an
 * exception is thrown.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response for invalid requests")
public class SpringErrorPayload {

  /**
   * A brief message describing the error.
   *
   * <p>This field provides a short description of the error that occurred.
   */
  @Schema(description = "A brief message describing the error", example = "Message")
  private String message;

  /**
   * Detailed information about the error.
   *
   * <p>This field provides more detailed information about the error, such as which field caused
   * the error and why.
   */
  @Schema(description = "Detailed information about the error", example = "Detailed description")
  private String details;

  /**
   * HTTP status code or custom application error code.
   *
   * <p>This field provides the HTTP status code or a custom application error code that corresponds
   * to the error.
   */
  @Schema(description = "HTTP status code or custom application error code", example = "418")
  private int status;

  @Override
  public String toString() {
    return "{\n"
            + "\"message\" = \"" + message + "\"\n"
            + "\"details\" = \"" + details + "\"\n"
            + "\"status\" = \"" + status + "\"\n"
            + "}";
  }
}
