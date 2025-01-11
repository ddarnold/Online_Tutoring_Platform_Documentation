package de.thu.thutorium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

/**
 * Global exception handler for managing various exceptions in the application. This class is
 * annotated with {@link ControllerAdvice} to indicate that it provides global exception handling.
 * It provides a centralized way to manage and respond to errors with appropriate HTTP status codes
 * and messages.
 *
 * <p>The {@link GlobalExceptionHandler} class catches exceptions thrown by the application and
 * returns a response with the appropriate error information, structured using the {@link
 * SpringErrorPayload} class. This ensures that error responses are consistent and follow the
 * structure defined by the {@link SpringErrorPayload} class, which is also documented in the
 * Swagger documentation through the {@link de.thu.thutorium.swagger.CommonApiResponses} annotation.
 *
 * @Author Jossin Anthony
 * @Author Nikolai Ivanov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /** Handles {@link MethodArgumentNotValidException} exceptions for validation errors. */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<SpringErrorPayload> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    String details =
        ex.getBindingResult().getAllErrors().stream()
            .map(
                error ->
                    String.format(
                        "Field '%s': %s",
                        ((FieldError) error).getField(), error.getDefaultMessage()))
            .collect(Collectors.joining("; "));

    SpringErrorPayload errorResponse =
        new SpringErrorPayload("Validation failed", details, HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /** Handles {@link HttpMessageNotReadableException} for invalid JSON input. */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<SpringErrorPayload> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    SpringErrorPayload errorResponse =
        new SpringErrorPayload(
            "Invalid input", ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /** Handles {@link ResourceNotFoundException} for missing resources. */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<SpringErrorPayload> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    SpringErrorPayload errorResponse =
        new SpringErrorPayload("Resource not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /** Handles {@link ResourceAlreadyExistsException} for duplicate resources. */
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<SpringErrorPayload> handleResourceAlreadyExistsException(
      ResourceAlreadyExistsException ex) {
    SpringErrorPayload errorResponse =
        new SpringErrorPayload("Resource conflict", ex.getMessage(), HttpStatus.CONFLICT.value());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  /** Handles {@link IllegalArgumentException} for invalid arguments. */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<SpringErrorPayload> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    SpringErrorPayload errorResponse =
        new SpringErrorPayload("Invalid argument", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MeetingConflictException.class)
  public ResponseEntity<String> handleMeetingConflictException(MeetingConflictException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());

  }

  // Generic Exception Handler (Fallback)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<SpringErrorPayload> handleGenericException(Exception ex) {
    SpringErrorPayload errorResponse =
        new SpringErrorPayload(
            "An unexpected error occurred.",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  //    /**
  //     * Handles {@link AuthenticationException} for authentication errors.
  //     */
  //    @ExceptionHandler(AuthenticationException.class)
  //    public ResponseEntity<SpringErrorPayload>
  // handleAuthenticationException(AuthenticationException ex) {
  //        SpringErrorPayload errorResponse = new SpringErrorPayload(
  //                "Authentication error",
  //                ex.getMessage(),
  //                HttpStatus.UNAUTHORIZED.value()
  //        );
  //        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  //    }
  //
  //    /**
  //     * Handles {@link UserNameNotFoundException} for missing user accounts.
  //     */
  //    @ExceptionHandler(UserNameNotFoundException.class)
  //    public ResponseEntity<SpringErrorPayload>
  // handleUsernameNotFoundException(UserNameNotFoundException ex) {
  //        SpringErrorPayload errorResponse = new SpringErrorPayload(
  //                "Username not found",
  //                ex.getMessage(),
  //                HttpStatus.NOT_FOUND.value()
  //        );
  //        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  //    }

}
