package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.MeetingTOMapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing meetings.
 *
 * <p>This service provides methods for creating, updating, deleting, and retrieving meetings. It
 * interacts with the {@link MeetingRepository}, {@link UserRepository}, {@link CourseRepository},
 * and {@link AddressRepository} for database operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {
  private final MeetingRepository meetingRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final AddressRepository addressRepository;
  private final MeetingTOMapper meetingTOMapper;
  private final MeetingDBMapper meetingDBMapper;

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
  public MeetingTO createMeeting(MeetingTO meetingTO) {
    // Fetch and validate the tutor
    UserDBO tutor =
        userRepository
            .findById(meetingTO.getTutorId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Tutor not found with ID: " + meetingTO.getTutorId()));

    // Fetch and validate the course
    CourseDBO course =
        courseRepository
            .findById(meetingTO.getCourseId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Course not found with ID: " + meetingTO.getCourseId()));

    // Fetch and validate the address
    AddressDBO address =
        addressRepository
            .findById(meetingTO.getAddressId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Address not found with ID: " + meetingTO.getAddressId()));

    // Fetch and validate participants
    List<UserDBO> participants =
        meetingTO.getParticipantIds().stream()
            .map(
                participantId ->
                    userRepository
                        .findById(participantId)
                        .orElseThrow(
                            () ->
                                new EntityNotFoundException(
                                    "Participant not found with ID: " + participantId)))
            .collect(Collectors.toList());

    // Convert the TO to the entity
    MeetingDBO meetingDBO = meetingDBMapper.toEntity(meetingTO);

    // Set references
    meetingDBO.setTutor(tutor);
    meetingDBO.setCourse(course);
    meetingDBO.setAddress(address);
    meetingDBO.setParticipants(participants);

    // Maintain bidirectional relationship for participants
    participants.forEach(participant -> participant.getMeetings().add(meetingDBO));

    // Save the meeting entity
    MeetingDBO savedMeeting = meetingRepository.save(meetingDBO);

    // Convert the saved entity back to a DTO
    return meetingTOMapper.toDTO(savedMeeting);
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
  public MeetingTO updateMeeting(Long meetingId, MeetingTO meetingTO) {
    // Fetch the existing meeting
    MeetingDBO existingMeeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    // Update fields
    existingMeeting.setMeetingDate(meetingTO.getMeetingDate());
    existingMeeting.setStartTime(meetingTO.getStartTime());
    existingMeeting.setEndTime(meetingTO.getEndTime());
    existingMeeting.setDuration(meetingTO.getDuration());
    existingMeeting.setMeetingType(meetingTO.getMeetingType());

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

    // Update participants
    List<UserDBO> newParticipants =
        meetingTO.getParticipantIds().stream()
            .map(
                participantId ->
                    userRepository
                        .findById(participantId)
                        .orElseThrow(
                            () ->
                                new EntityNotFoundException(
                                    "Participant not found with ID: " + participantId)))
            .collect(Collectors.toList());

    // Remove participants who are no longer part of the meeting
    existingMeeting
        .getParticipants()
        .forEach(
            participant -> {
              if (!newParticipants.contains(participant)) {
                participant.getMeetings().remove(existingMeeting);
              }
            });

    // Add new participants
    newParticipants.forEach(
        participant -> {
          if (!existingMeeting.getParticipants().contains(participant)) {
            participant.getMeetings().add(existingMeeting);
          }
        });

    // Set the updated list of participants
    existingMeeting.setParticipants(newParticipants);

    // Update optional fields (room number, campus name, university name)
    existingMeeting.setRoomNum(meetingTO.getRoomNum());

    // Save the updated meeting
    MeetingDBO updatedMeeting = meetingRepository.save(existingMeeting);

    // Convert the updated entity back to DTO
    return meetingTOMapper.toDTO(updatedMeeting);
  }

  /**
   * Retrieves all meetings associated with a specific user.
   *
   * <p>This method fetches both types of meetings related to the user: - Meetings in which the user
   * is a participant. - Meetings scheduled by the user as a tutor.
   *
   * <p>The two lists are combined, and the resulting list of meetings is mapped to DTOs for easier
   * representation.
   *
   * @param userId the unique identifier of the user whose meetings are to be retrieved
   * @return a list of {@link MeetingTO} objects representing the meetings related to the user
   */
  @Override
  public List<MeetingTO> getMeetingsForUser(Long userId) {
    // Get both participated and scheduled meetings
    List<MeetingDBO> participatedMeetings =
        meetingRepository.findParticipatedMeetingsByUserId(userId);
    List<MeetingDBO> scheduledMeetings = meetingRepository.findScheduledMeetingsByTutorId(userId);

    // Combine both lists
    List<MeetingDBO> allMeetings = new ArrayList<>();
    allMeetings.addAll(participatedMeetings);
    allMeetings.addAll(scheduledMeetings);

    // Map to DTO
    return meetingTOMapper.toDTOList(allMeetings);
  }
}
