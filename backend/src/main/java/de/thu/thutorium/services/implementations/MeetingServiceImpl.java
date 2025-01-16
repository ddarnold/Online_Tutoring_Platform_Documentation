package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.MeetingTOMapper;
import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.database.dbObjects.AddressDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.MeetingDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
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
import java.util.Optional;

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
// Todo: The class methods have not been tested by the developer
//  Update documentation in both interface and implementation classes.
//  provide the appropriate controller methods utilizing these methods.
public class MeetingServiceImpl implements MeetingService {
  private final MeetingRepository meetingRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final AddressRepository addressRepository;
  private final MeetingTOMapper meetingTOMapper;
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
    // Todo : The user should have been verified by the admin in order to be able to create a meeting for a course.
    //  To be implemented in the verifier Service class.
    //Check if the tutor is valid
    UserDBO tutor = userRepository.findUserDBOByUserIdAndRoles_RoleName(meetingTO.getTutorId(), Role.TUTOR)
            .orElseThrow(() -> new EntityNotFoundException("Tutor not found with ID: " + meetingTO.getTutorId()));

    //Check if the course is valid
    CourseDBO course = courseRepository.findByCourseIdAndTutor_UserId(meetingTO.getCourseId(), meetingTO.getTutorId())
            .orElseThrow(() -> new EntityNotFoundException("Course with ID: " + meetingTO.getCourseId() + " not found from "
            + " tutor with ID" + meetingTO.getTutorId()));

    //Check if the address is valid
    AddressDBO address = addressRepository.findById(meetingTO.getAddressId())
            .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + meetingTO.getAddressId()));

    String roomNum = Optional.ofNullable(meetingTO.getRoomNum())
        .orElse("No room scheduled");
    String meetingLink = Optional.ofNullable(meetingTO.getMeetingLink())
        .orElse("No meeting link provided");


    MeetingDBO meetingDBO = MeetingDBO.builder()
            .tutor(tutor)
            .course(course)
            .startTime(meetingTO.getStartTime())
            .endTime(meetingTO.getEndTime())
            .meetingType(meetingTO.getMeetingType())
            .roomNum(roomNum)
            .meetingLink(meetingLink)
            .address(address)
            .build();
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
    MeetingDBO meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new EntityNotFoundException("Meeting not found with ID: " + meetingId)
    );

    for (UserDBO participant: meeting.getParticipants()) {
      participant.getMeetings().remove(meeting);
      userRepository.save(participant);
    }

    meeting.getTutor().getMeetings().remove(meeting);
    userRepository.save(meeting.getTutor());

    meeting.getCourse().getMeetings().remove(meeting);
    courseRepository.save(meeting.getCourse());

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
    MeetingDBO existingMeeting = meetingRepository.findById(meetingId)
                    .orElseThrow(() -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    //Validate tutor, course and address
    //Check if the tutor is valid
    UserDBO tutor = userRepository.findUserDBOByUserIdAndRoles_RoleName(meetingTO.getTutorId(), Role.TUTOR)
            .orElseThrow(() -> new EntityNotFoundException("Tutor not found with ID: " + meetingTO.getTutorId()));

    //Check if the course is valid
    CourseDBO course = courseRepository.findByCourseIdAndTutor_UserId(meetingTO.getCourseId(), meetingTO.getTutorId())
            .orElseThrow(() -> new EntityNotFoundException("Course with ID: " + meetingTO.getCourseId() + " not found from "
                    + " tutor with ID" + meetingTO.getTutorId()));

    //Check if the address is valid
    AddressDBO address = addressRepository.findById(meetingTO.getAddressId())
            .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + meetingTO.getAddressId()));

    //Handle the optional fields in the meetingTO
    String roomNum = Optional.ofNullable(meetingTO.getRoomNum())
            .orElse("No room scheduled");
    String meetingLink = Optional.ofNullable(meetingTO.getMeetingLink())
            .orElse("No meeting link provided");

    // Update fields
    existingMeeting = existingMeeting.toBuilder()
            .tutor(tutor)
            .course(course)
            .startTime(meetingTO.getStartTime())
            .endTime(meetingTO.getEndTime())
            .meetingType(meetingTO.getMeetingType())
            .roomNum(roomNum)
            .meetingLink(meetingLink)
            .address(address)
            .build();
    // Update participants
    //Todo: Adding and deleting participants is not done via MeetingTO, as it can be
    // error prone and cumbersome (e.g: editing participant details of 20 students). Define
    // other methods e.g; bookMeeting() and cancelMeeting() (similar to enroll/unenroll courses)
    // to achieve this.
    // Also single responsibility principle.

    //Save the updated meeting
    MeetingDBO savedMeeting = meetingRepository.save(existingMeeting);
    return meetingTOMapper.toDTO(savedMeeting);
  }

  /**
   * Todo :
   *  What is the purpose of getting all meetings both scheduled and participating?
   *  Shouldn't these be separate functions?
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

    UserDBO user = userRepository.findUserDBOByUserId(userId).orElseThrow(
            () -> new EntityNotFoundException("User not found with ID: " + userId)
    );

    List<MeetingDBO> allMeetings = new ArrayList<>(user.getMeetings());
    allMeetings.addAll(user.getMeetingsScheduled());
    // @Todo : Test the alternate implementation
    //
//    // Get both participated and scheduled meetings
//    List<MeetingDBO> participatedMeetings =
//        meetingRepository.findParticipatedMeetingsByUserId(userId);
//    List<MeetingDBO> scheduledMeetings = meetingRepository.findScheduledMeetingsByTutorId(userId);
//
//    // Combine both lists
//
//    allMeetings.addAll(participatedMeetings);
//    allMeetings.addAll(scheduledMeetings);

    // Map to DTO
    return meetingTOMapper.toDTOList(allMeetings);
  }

  //Todo: Booking of a meeting by a student
  @Override
  @Transactional
  public void bookMeeting(Long meetingId) {

    //Retrieve user and confirm user is a student
    //check if meeting exist, and is in future
    //Check if student is enrolled in a course, for which the meeting is scheduled

    // Update the user meetings list
    // update the meetings participant list
  }

  //Todo: Cancellation of a meeting by a student
  @Transactional
  @Override
  public void cancelMeeting(Long meetingId) {
    //Retrieve user and confirm user is a participant of the meeting

    // Update the user meetings list
    // update the meetings participant list
  }

  // Todo : Meant for the tutor -> in case if it is not implemented already
  @Override
  public MeetingTO retrieveMeetingbyId(Long meetingId) {
    return null;
  }

  // Todo : Meant for the tutor -> in case if it is not implemented already
  @Override
  public MeetingTO retrieveMeetingbyCourse(Long courseId) {
    return null;
  }

  // Todo : Meant for the tutor -> in case if it is not implemented already
  @Override
  public void retrieveAllParticipants(Long meetingId) {
    return;
  }
}
