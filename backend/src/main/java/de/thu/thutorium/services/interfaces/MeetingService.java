package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import org.springframework.stereotype.Service;

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
   */
  void createMeeting(MeetingTO meetingTO);

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
  void updateMeeting(Long meetingId, MeetingTO meetingTO);
}
