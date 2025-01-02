package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.api.transferObjects.common.UserTO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.services.interfaces.MeetingService;
import de.thu.thutorium.services.interfaces.UserService;
import de.thu.thutorium.swagger.CommonApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller for managing user operations. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User end-points", description = "Endpoints for user operations")
@Slf4j
@CommonApiResponses
public class UserController {

  private final UserService userService;
  private final MeetingService meetingService;

  /**
   * Retrieves the account details of a user based on their user ID.
   *
   * @param userId the unique identifier of the user, extracted from the query parameter
   * @return the {@link UserTO} object containing the account details
   * @throws UsernameNotFoundException if the use could not be found in the database.
   */
  @Operation(
      summary = "Retrieve the account details of an existing user",
      description = "Retrieve an existing user if they exist in the database",
      tags = {" User Endpoints"})
  @CommonApiResponses
  @GetMapping("/get-user/{userId}")
  public ResponseEntity<?> getUser(@PathVariable Long userId) {
    try {
      UserTO user = userService.findByUserId(userId);
      return ResponseEntity.status(HttpStatus.OK).body(user);
    } catch (UsernameNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Updates an existing user.
   *
   * @param id the id of the user to be updated
   * @param userTO {@code UserTO}, contains the information to be updated.
   * @return a {@code ResponseEntity} containing the updated {@code UserTO} object and a {@link
   *     org.springframework.http.HttpStatus#OK} status
   * @throws UsernameNotFoundException if the use could not be found in the database.
   */
  @Operation(
      summary = "Update an existing user",
      description = "Update an existing user if they exist in the database",
      tags = {" User Endpoints"})
  @CommonApiResponses
  @PutMapping("/update-user/{id}")
  public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserTO userTO) {
    try {
      UserTO updatedUser = userService.updateUser(id, userTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(updatedUser);
    } catch (UsernameNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + ex.getMessage());
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unexpected error: " + ex.getMessage());
    }
  }

  /**
   * Deletes the account details of a user based on their user ID.
   *
   * @return a {@code ResponseEntity} containing the status of successful deletion {@link
   *     org.springframework.http.HttpStatus#OK}
   * @throws AuthenticationException if the user could not be found as authenticated in the
   *     database.
   */
  @Operation(
      summary = "Enables an existing user to delete his account",
      description = "Retrieves an existing and authenticated user and deletes their account.",
      tags = {" User Endpoints"})
  @CommonApiResponses
  @DeleteMapping("/delete-my-account")
  public ResponseEntity<String> deleteMyAccount() {
    try {
      // Retrieve the currently authenticated user's ID
      Long authenticatedUserId = getAuthenticatedUserId();
      userService.deleteUser(authenticatedUserId);
      return ResponseEntity.ok(
          "User account with ID " + authenticatedUserId + " has been successfully deleted.");
    } catch (AuthenticationException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred while deleting the account.");
    }
  }

  /**
   * Retrieves a tutor by their ID.
   *
   * @param id the ID of the tutor to retrieve.
   * @return the {@link UserTO} object containing tutor details.
   */
  @Operation(
      summary = "Retrieve tutor by ID",
      description = "Fetches tutor details by their unique ID.",
      tags = {"User Operations"})
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Tutor retrieved successfully",
        content = @Content(schema = @Schema(implementation = UserTO.class))),
    @ApiResponse(responseCode = "404", description = "Tutor not found")
  })
  @GetMapping("tutor")
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public UserTO getTutor(@RequestParam Long id) {
    return userService.getTutorByID(id);
  }

  /**
   * Retrieves all meetings associated with a specific user.
   *
   * <p>This endpoint fetches a list of meetings for a given user ID. It includes both:
   *
   * <ul>
   *   <li>Meetings where the user is a participant
   *   <li>Meetings scheduled by the user in their role as a tutor
   * </ul>
   *
   * @param userId the unique identifier of the user whose meetings are to be retrieved
   * @return a {@link ResponseEntity} containing a list of {@link MeetingTO} objects representing
   *     the meetings associated with the user, or an empty list if none are found
   * @throws org.springframework.web.server.ResponseStatusException if the user is not found or an
   *     error occurs while retrieving the meetings
   * @see MeetingTO
   */
  @Operation(
      summary = "Retrieve all meetings for a specific user",
      description =
          "Fetches a list of meetings associated with a user. Includes both meetings the user participates in and meetings they have scheduled as a tutor.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Meetings retrieved successfully",
        content =
            @Content(array = @ArraySchema(schema = @Schema(implementation = MeetingTO.class)))),
    @ApiResponse(
        responseCode = "404",
        description = "User not found or no meetings available for the user",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  @GetMapping("/get-meetings/{userId}")
  public ResponseEntity<List<MeetingTO>> getMeetingsForUser(@PathVariable Long userId) {
    List<MeetingTO> meetings = meetingService.getMeetingsForUser(userId);
    return ResponseEntity.ok(meetings);
  }

  /**
   * Retrieves the authenticated user's ID.
   *
   * @return the ID of the authenticated user.
   * @throws AuthenticationException if the user is not authenticated.
   */
  private Long getAuthenticatedUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AuthenticationException("User is not authenticated") { };
    }

    UserDBO userDetails = (UserDBO) authentication.getPrincipal();
    return userDetails.getUserId();
  }

}
