package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.authentication.LogInRequestTO;
import de.thu.thutorium.api.transferObjects.authentication.LogInResponseTO;
import de.thu.thutorium.api.transferObjects.authentication.RegisterRequestTO;
import de.thu.thutorium.exceptions.ResourceAlreadyExistsException;
import de.thu.thutorium.services.implementations.AuthenticationServiceImpl;
import de.thu.thutorium.swagger.CommonApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthenticationController is a REST controller that handles authentication-related requests. It
 * provides endpoints for user login and other authentication operations.
 *
 * <p>This controller uses the AuthenticationService to perform the actual authentication logic.
 * ToDo : Testing
 */
@RestController
@RequestMapping("/auth")
@CommonApiResponses
@RequiredArgsConstructor
// @Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthenticationController {

  private final AuthenticationServiceImpl authService;

  /**
   * Registers a user based on the provided register request.
   *
   * @param request the register request transfer object containing user credentials and roles.
   * @return a ResponseEntity containing the login response transfer object ToDo: Testing, Document
   *     error codes
   */
  @Operation(
      summary = "Register a new user",
      description = "Registers a new user with the provided credentials and roles")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "Registered successfully",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = LogInResponseTO.class))))
  @CommonApiResponses
  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestTO request) {
    try {
      return authService.register(request);
    } catch (ResourceAlreadyExistsException ex) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected Error: " + ex.getMessage());
    }
  }

  /**
   * Authenticates a user based on the provided login request.
   *
   * @param request the login request transfer object containing user credentials
   * @return a ResponseEntity containing the login response transfer object ToDo: Test, Document all
   *     error codes
   */
  @Operation(
      summary = "Authenticate a new user",
      description = "Authenticates a user with the provided credentials and roles")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "Logged in successfully",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = LogInResponseTO.class))))
  @PostMapping("/login")
  public ResponseEntity<?> authenticate(@Valid @RequestBody LogInRequestTO request) {
    try {
      return authService.authenticate(request);
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected Error: " + ex.getMessage());
    }
  }

  /**
   * Activate the account of a newly registered user.
   *
   * <p>A verification link is sent to a newly registered user. The user account is activated after
   * the user confirms his identity by clicking on the link, and subsequent validation of the token
   * sent to the backend.
   *
   * @param token the token which is validated to confirm the users identity
   * @return String message confirming the verification. ToDo: Test, Document all error codes
   */
  @Operation(
      summary = "Activate a new user account",
      description =
          "Activates a new user account after they confirm their identity by "
              + "utilizing the verification link")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "Verified successfully",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = String.class))))
  @GetMapping("/verify")
  public String verifyUserEmail(@RequestParam("token") String token) {
    // Lookup token in the database
    // Implement token validation and user activation
    return "Should receive: Account verification message";
  }

  /**
   * Send password reset email.
   *
   * <p>A reset email is sent to the user to initiate password reset, if the user already exists in
   * the records. The email contains a reset token.
   *
   * @param email the email to which the rest password link is sent, if it exists in the database.
   * @return String message confirming the sending of the reset email. ToDo: Test, Document all
   *     error codes
   */
  @Operation(
      summary = "Initiate password reset",
      description = "Sends an existing user an email link to initiate the reset of password")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "Password reset email sent!",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = String.class))))
  @PostMapping("/reset-password")
  public String initiateResetPassword(@RequestParam("email") String email) {
    return "Should receive: Password reset email sent message";
  }

  /**
   * Endpoint to validate and reset password.
   *
   * <p>The password is successfully reset if it adheres to the password guidelines after successful
   * validation of the received token.
   *
   * @param token the verification token from the user as proof of identity.
   * @param newPassword the new password.
   * @return String message confirming successful reset of password. ToDo: Test, Document all error
   *     codes
   */
  @Operation(
      summary = "Execute adn confirm password reset",
      description = "Validates and resets the account password")
  @ApiResponses(
      @ApiResponse(
          responseCode = "200",
          description = "Password reset successfully",
          content =
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = String.class))))
  @PostMapping("/reset/confirm")
  public String confirmResetPassword(
      @RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
    // Find user by reset token
    return "Should receive: Password reset successful message";
  }
}
