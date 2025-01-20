package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.api.transferObjects.common.UserTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code MeetingService} interface provides methods for managing meetings.
 *
 * <p>It exposes the following functionalities:
 *
 * <ul>
 *   <li>Create a new meeting.
 *   <li>Delete an existing meeting by its ID.
 *   <li>Update an existing meeting's details.
 * </ul>
 */
@Service
public interface MeetingService {
  /**
   * Creates a new meeting.
   *
   * <p>This method creates a new meeting in the system based on the provided {@link MeetingTO}
   * object.
   *
   * @param meetingTO the {@link MeetingTO} object containing the data for the meeting to be
   *     created.
   * @return {@link MeetingTO} of the new {@link de.thu.thutorium.database.dbObjects.MeetingDBO}
   *     object created.
   */
  MeetingTO createMeeting(MeetingTO meetingTO);

  /**
   * Deletes a meeting by its unique ID.
   *
   * <p>This method deletes the meeting with the specified ID from the system.
   *
   * @param meetingId the unique ID of the meeting to be deleted.
   */
  void deleteMeeting(Long meetingId);

  /**
   * Updates the details of an existing meeting.
   *
   * <p>This method updates the meeting data based on the provided {@link MeetingTO} object.
   *
   * @param meetingId the unique ID of the meeting to be updated.
   * @param meetingTO the {@link MeetingTO} object containing the new meeting data.
   */
  MeetingTO updateMeeting(Long meetingId, MeetingTO meetingTO);

  /**
   * Retrieves all meetings associated with a specific user.
   *
   * <p>This method returns a list of meetings related to the user, including: - Meetings
   * in which
   * the user is a participant. - Meetings scheduled by the user as a tutor.
   *
   * <p>The combined list of meetings is mapped to {@link MeetingTO} objects for easier use in
   * service layers or client responses.
   *
   * @param userId the unique identifier of the user whose meetings are to be retrieved
   * @return a list of {@link MeetingTO} objects representing the meetings related to the user
   */
  List<MeetingTO> getMeetingsForUser(Long userId);

  /**
   * Books a meeting for the authenticated student.
   *
   * <p>This method retrieves the authenticated student's ID, validates the student, and
   * checks if the student is enrolled in the course for the meeting. If the student is
   * enrolled, the student is added to the meeting's participants and the meeting is
   * added to the student's meetings. The changes are then saved to the repository.
   *
   * @param meetingId the ID of the meeting to be booked
   * @throws jakarta.persistence.EntityNotFoundException if the meeting or student is not found
   * @throws IllegalStateException if the student is not enrolled in the course for the meeting
   */
  void bookMeeting(Long meetingId);

  /**
   * Cancels a meeting for the authenticated student.
   *
   * <p>This method retrieves the authenticated student's ID, validates the student, and checks if
   * the student is a participant of the meeting. If the student is a participant, the student is
   * removed from the meeting's participants and the meeting is removed from the student's meetings.
   * The changes are then saved to the repository.
   *
   * @param meetingId the ID of the meeting to be canceled
   * @throws jakarta.persistence.EntityNotFoundException if the meeting or student is not found
   * @throws IllegalStateException if the student is not a participant of the meeting
   */
  void cancelMeeting(Long meetingId);

  /**
   * Retrieves a meeting by its ID.
   *
   * <p>This method retrieves a meeting by its ID and maps it to a transfer object (DTO).
   *
   * @param meetingId the ID of the meeting to be retrieved
   * @return the meeting transfer object
   * @throws jakarta.persistence.EntityNotFoundException if the meeting is not found
   */
  MeetingTO retrieveMeetingById(Long meetingId);

  /**
   * Retrieves all meetings for a specific course.
   *
   * <p>This method retrieves a course by its ID and returns a list of meetings for that course,
   * mapped to transfer objects (DTOs).
   *
   * @param courseId the ID of the course
   * @return a list of meeting transfer objects
   * @throws jakarta.persistence.EntityNotFoundException if the course is not found
   */
  List<MeetingTO> retrieveMeetingsByCourse(Long courseId);

  /**
   * Retrieves all participants for a specific meeting.
   *
   * <p>This method retrieves a meeting by its ID and returns a list of participants for that meeting,
   * mapped to transfer objects (DTOs).
   *
   * @param meetingId the ID of the meeting
   * @return a list of user transfer objects
   * @throws jakarta.persistence.EntityNotFoundException if the meeting is not found
   */
  List<UserTO> retrieveAllParticipants(Long meetingId);
  }
