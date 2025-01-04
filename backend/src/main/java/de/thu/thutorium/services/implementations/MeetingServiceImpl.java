package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.database.DBOMappers.MeetingDBMapper;
import de.thu.thutorium.database.dbObjects.AddressDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.MeetingDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.AddressRepository;
import de.thu.thutorium.database.repositories.CourseRepository;
import de.thu.thutorium.database.repositories.MeetingRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.MeetingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing meetings.
 *
 * <p>This service provides methods for creating, updating, deleting, and retrieving meetings. It
 * interacts with the {@link MeetingRepository}, {@link UserRepository}, {@link CourseRepository},
 * and {@link AddressRepository} for database operations, and uses {@link MeetingDBMapper} for
 * mapping between DTO and DBO.
 */
@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
  private final MeetingRepository meetingRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final AddressRepository addressRepository;
  private final MeetingDBMapper meetingMapper;

  /**
   * Creates a new meeting based on the provided {@link MeetingTO}.
   *
   * <p>This method fetches the tutor, course, and address from the database using the provided IDs
   * in the meeting transfer object (DTO). It then maps the meeting DTO to a meeting entity (DBO),
   * associates the tutor, course, and address, and saves the new meeting in the database.
   *
   * @param meetingTO the transfer object containing the meeting data to be created
   * @throws EntityNotFoundException if the tutor, course, or address with the provided IDs are not
   *     found
   */
  @Override
  @Transactional
  public void createMeeting(MeetingTO meetingTO) {
    UserDBO tutor =
        userRepository
            .findById(meetingTO.getTutorId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Tutor not found with ID: " + meetingTO.getTutorId()));
    CourseDBO course =
        courseRepository
            .findById(meetingTO.getCourseId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Course not found with ID: " + meetingTO.getCourseId()));
    AddressDBO address =
        addressRepository
            .findById(meetingTO.getAddressId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Address not found with ID: " + meetingTO.getAddressId()));

    // Calculate new meeting's time range
    LocalDateTime newMeetingStart = meetingTO.getMeetingTime();
    LocalDateTime newMeetingEnd = newMeetingStart.plusMinutes(meetingTO.getDuration());

    // Fetch potential overlaps
    List<MeetingDBO> overlappingMeetings =
        meetingRepository
            .findByMeetingDateAndRoomNumAndMeetingTimeLessThanEqualAndMeetingTimeGreaterThanEqual(
                meetingTO.getMeetingDate(), meetingTO.getRoomNum(), newMeetingEnd, newMeetingStart);

    // Check for actual overlaps
    for (MeetingDBO existingMeeting : overlappingMeetings) {
      LocalDateTime existingMeetingStart = existingMeeting.getMeetingTime();
      LocalDateTime existingMeetingEnd =
          existingMeetingStart.plusMinutes(existingMeeting.getDuration());

      if (newMeetingStart.isBefore(existingMeetingEnd)
          && newMeetingEnd.isAfter(existingMeetingStart)) {
        throw new IllegalArgumentException(
            "A meeting is already scheduled during the requested time in room "
                + meetingTO.getRoomNum());
      }
    }

    MeetingDBO meetingDBO = meetingMapper.toEntity(meetingTO);

    // Set References
    meetingDBO.setTutor(tutor);
    meetingDBO.setCourse(course);
    meetingDBO.setAddress(address);

    meetingRepository.save(meetingDBO);
  }

  /**
   * Deletes an existing meeting by its ID.
   *
   * <p>This method checks if a meeting with the provided ID exists in the database. If it does, the
   * meeting is deleted; otherwise, an {@link EntityNotFoundException} is thrown.
   *
   * @param meetingId the ID of the meeting to be deleted
   * @throws EntityNotFoundException if the meeting with the provided ID does not exist
   */
  @Override
  @Transactional
  public void deleteMeeting(Long meetingId) {
    if (!meetingRepository.existsById(meetingId)) {
      throw new EntityNotFoundException("Meeting not found with ID: " + meetingId);
    }

    // Delete the meeting
    meetingRepository.deleteById(meetingId);
  }

  /**
   * Updates the details of an existing meeting.
   *
   * <p>This method fetches an existing meeting by its ID, updates its fields based on the provided
   * {@link MeetingTO}, and saves the updated meeting entity in the database. If the meeting, tutor,
   * course, or address is not found, an {@link EntityNotFoundException} is thrown.
   *
   * @param meetingId the ID of the meeting to be updated
   * @param meetingTO the transfer object containing the updated meeting data
   * @throws EntityNotFoundException if the meeting, tutor, course, or address with the provided IDs
   *     are not found
   */
  @Override
  @Transactional
  public void updateMeeting(Long meetingId, MeetingTO meetingTO) {
    // Fetch the existing meeting
    MeetingDBO existingMeeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    // Update fields
    existingMeeting.setMeetingDate(meetingTO.getMeetingDate());
    existingMeeting.setMeetingTime(meetingTO.getMeetingTime());
    existingMeeting.setDuration(meetingTO.getDuration());
    existingMeeting.setMeetingType(meetingTO.getMeetingType());
    existingMeeting.setMeetingStatus(meetingTO.getMeetingStatus());

    // Update associated objects (tutor, course, and address)
    UserDBO tutor =
        userRepository
            .findById(meetingTO.getTutorId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Tutor not found with ID: " + meetingTO.getTutorId()));
    existingMeeting.setTutor(tutor);

    CourseDBO course =
        courseRepository
            .findById(meetingTO.getCourseId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Course not found with ID: " + meetingTO.getCourseId()));
    existingMeeting.setCourse(course);

    AddressDBO address =
        addressRepository
            .findById(meetingTO.getAddressId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Address not found with ID: " + meetingTO.getAddressId()));
    existingMeeting.setAddress(address);

    // Save the updated meeting
    meetingRepository.save(existingMeeting);
  }
}
