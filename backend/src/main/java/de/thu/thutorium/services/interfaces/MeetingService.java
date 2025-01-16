package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
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
   * <p>This method returns a list of meetings related to the user, including: - Meetings in which
   * the user is a participant. - Meetings scheduled by the user as a tutor.
   *
   * <p>The combined list of meetings is mapped to {@link MeetingTO} objects for easier use in
   * service layers or client responses.
   *
   * @param userId the unique identifier of the user whose meetings are to be retrieved
   * @return a list of {@link MeetingTO} objects representing the meetings related to the user
   */
  List<MeetingTO> getMeetingsForUser(Long userId);

  void bookMeeting(Long meetingId);

  void cancelMeeting(Long meetingId);

  MeetingTO retrieveMeetingbyId(Long meetingId);

  MeetingTO retrieveMeetingbyCourse(Long courseId);

  void retrieveAllParticipants(Long meetingId);
}
