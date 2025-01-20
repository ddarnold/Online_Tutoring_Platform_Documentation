package de.thu.thutorium.services.implementations;

import de.thu.thutorium.Utility.AuthUtil;
import de.thu.thutorium.api.TOMappers.MeetingTOMapper;
import de.thu.thutorium.api.TOMappers.UserTOMapper;
import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.api.transferObjects.common.UserTO;
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
// Todo: test
// TODO:Update documentation in both interface and implementation classes.
// TODO: provide the appropriate controller methods utilizing these methods.
public class MeetingServiceImpl implements MeetingService {
  private final MeetingRepository meetingRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final AddressRepository addressRepository;
  private final MeetingTOMapper meetingTOMapper;
  private final UserTOMapper userTOMapper;

  private static final String DEFAULT_ROOM_NUM = "No room scheduled";
  private static final String DEFAULT_MEETING_LINK = "No room scheduled";

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

    validateTutorVerified(meetingTO.getTutorId());
    UserDBO tutor = validateAndGetTutor(meetingTO.getTutorId());
    CourseDBO course = validateAndGetCourse(meetingTO.getCourseId(), meetingTO.getTutorId());

    MeetingDBO meetingDBO =
        MeetingDBO.builder()
            .tutor(tutor)
            .course(course)
            .startTime(meetingTO.getStartTime())
            .endTime(meetingTO.getEndTime())
            .meetingType(meetingTO.getMeetingType())
            .roomNum(Optional.ofNullable(meetingTO.getRoomNum()).orElse(DEFAULT_ROOM_NUM))
            .meetingLink(
                Optional.ofNullable(meetingTO.getMeetingLink()).orElse(DEFAULT_MEETING_LINK))
            .build();

    if (meetingTO.getAddressId() != null) {
      AddressDBO address = validateAndGetAddress(meetingTO.getAddressId());
      meetingDBO.setAddress(address);
    }

    return meetingTOMapper.toDTO(meetingRepository.save(meetingDBO));

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
    MeetingDBO meeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    meeting
        .getParticipants()
        .forEach(
            participant -> {
              participant.getMeetings().remove(meeting);
              userRepository.save(participant);
            });

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
    MeetingDBO existingMeeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    UserDBO tutor = validateAndGetTutor(meetingTO.getTutorId());
    CourseDBO course = validateAndGetCourse(meetingTO.getCourseId(), meetingTO.getTutorId());

    existingMeeting =
        existingMeeting.toBuilder()
            .tutor(tutor)
            .course(course)
            .startTime(meetingTO.getStartTime())
            .endTime(meetingTO.getEndTime())
            .meetingType(meetingTO.getMeetingType())
            .roomNum(Optional.ofNullable(meetingTO.getRoomNum()).orElse(DEFAULT_ROOM_NUM))
            .meetingLink(
                Optional.ofNullable(meetingTO.getMeetingLink()).orElse(DEFAULT_MEETING_LINK))
            .build();

    if (meetingTO.getAddressId() != null) {
      AddressDBO address = validateAndGetAddress(meetingTO.getAddressId());
      existingMeeting.setAddress(address);
    }

    return meetingTOMapper.toDTO(meetingRepository.save(existingMeeting));
  }

  /**
   * This method fetches both types of meetings related to the user: - Meetings in which the user is
   * a participant. - Meetings scheduled by the user as a tutor.
   *
   * <p>The two lists are combined, and the resulting list of meetings is mapped to DTOs for easier
   * representation.
   *
   * @param userId the unique identifier of the user whose meetings are to be retrieved
   * @return a list of {@link MeetingTO} objects representing the meetings related to the user
   */
  @Override
  public List<MeetingTO> getMeetingsForUser(Long userId) {
    UserDBO user =
        userRepository
            .findUserDBOByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    List<MeetingDBO> allMeetings = new ArrayList<>(user.getMeetings());
    allMeetings.addAll(user.getMeetingsScheduled());

    return meetingTOMapper.toDTOList(allMeetings);
  }


  /**
   * Books a meeting for the authenticated student.
   *
   * <p>This method retrieves the authenticated student's ID, validates the student, and checks if the student is
   * enrolled in the course for the meeting. If the student is enrolled, the student is added to the meeting's
   * participants and the meeting is added to the student's meetings. The changes are then saved to the repository.
   *
   * @param meetingId the ID of the meeting to be booked
   * @throws EntityNotFoundException if the meeting or student is not found
   * @throws IllegalStateException if the student is not enrolled in the course for the meeting
   */
  @Override
  @Transactional
  public void bookMeeting(Long meetingId) {
    Long studentId = AuthUtil.getAuthenticatedUserId(); // Retrieve the authenticated user's ID

    UserDBO student = validateAndGetStudent(studentId);

    MeetingDBO meeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    if (!meeting.getCourse().getStudents().contains(student)) {
      throw new IllegalStateException("Student is not enrolled in the course for this meeting.");
    }

    meeting.getParticipants().add(student);
    student.getMeetings().add(meeting);

    meetingRepository.save(meeting);
    userRepository.save(student);
  }

  /**
   * Cancels a meeting for the authenticated student.
   *
   * <p>This method retrieves the authenticated student's ID, validates the student, and checks if the student is a
   * participant of the meeting. If the student is a participant, the student is removed from the meeting's participants
   * and the meeting is removed from the student's meetings. The changes are then saved to the repository.
   *
   * @param meetingId the ID of the meeting to be canceled
   * @throws EntityNotFoundException if the meeting or student is not found
   * @throws IllegalStateException if the student is not a participant of the meeting
   */
  @Override
  @Transactional
  public void cancelMeeting(Long meetingId) {
    Long studentId = AuthUtil.getAuthenticatedUserId(); // Retrieve the authenticated user's ID

    UserDBO student = validateAndGetStudent(studentId);

    MeetingDBO meeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    if (!meeting.getParticipants().contains(student)) {
      throw new IllegalStateException("Student is not a participant of this meeting.");
    }

    meeting.getParticipants().remove(student);
    student.getMeetings().remove(meeting);

    meetingRepository.save(meeting);
    userRepository.save(student);
  }

  /**
   * Retrieves a meeting by its ID.
   *
   * <p>This method retrieves a meeting by its ID and maps it to a transfer object (DTO).
   *
   * @param meetingId the ID of the meeting to be retrieved
   * @return the meeting transfer object
   * @throws EntityNotFoundException if the meeting is not found
   */
  @Override
  public MeetingTO retrieveMeetingById(Long meetingId) {
    return meetingRepository
        .findById(meetingId)
        .map(meetingTOMapper::toDTO)
        .orElseThrow(() -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));
  }

  /**
   * Retrieves all meetings for a specific course.
   *
   * <p>This method retrieves a course by its ID and returns a list of meetings for that course, mapped to transfer objects (DTOs).
   *
   * @param courseId the ID of the course
   * @return a list of meeting transfer objects
   * @throws EntityNotFoundException if the course is not found
   */
  @Override
  public List<MeetingTO> retrieveMeetingsByCourse(Long courseId) {
    CourseDBO course =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () -> new EntityNotFoundException("Course not found with ID: " + courseId));

    return meetingTOMapper.toDTOList(new ArrayList<>(course.getMeetings()));
  }

  /**
   * Retrieves all participants for a specific meeting.
   *
   * <p>This method retrieves a meeting by its ID and returns a list of participants for that meeting, mapped to transfer objects (DTOs).
   *
   * @param meetingId the ID of the meeting
   * @return a list of user transfer objects
   * @throws EntityNotFoundException if the meeting is not found
   */
  @Override
  public List<UserTO> retrieveAllParticipants(Long meetingId) {
    MeetingDBO meeting =
        meetingRepository
            .findById(meetingId)
            .orElseThrow(
                () -> new EntityNotFoundException("Meeting not found with ID: " + meetingId));

    return meeting.getParticipants().stream().map(userTOMapper::toDTO).toList();
  }

  /**
   * Validates if a tutor is verified.
   *
   * <p>This method checks if a tutor is verified by their ID.
   *
   * @param tutorId the ID of the tutor
   * @throws IllegalStateException if the tutor is not verified
   */
  private void validateTutorVerified(Long tutorId) {
    if (!isTutorVerified(tutorId)) {
      throw new IllegalStateException("Tutor must be verified to create a meeting.");
    }
  }

  // helper methods
  /**
   * Validates and retrieves a tutor by their ID.
   *
   * @param tutorId the ID of the tutor
   * @return the tutor entity
   * @throws EntityNotFoundException if the tutor is not found
   */
  private UserDBO validateAndGetTutor(Long tutorId) {
    return userRepository
        .findUserDBOByUserIdAndRoles_RoleName(tutorId, Role.TUTOR)
        .orElseThrow(() -> new EntityNotFoundException("Tutor not found with ID: " + tutorId));
  }

  /**
   * Validates and retrieves a student by their ID.
   *
   * @param studentId the ID of the student
   * @return the student entity
   * @throws EntityNotFoundException if the student is not found
   */
  private UserDBO validateAndGetStudent(Long studentId) {
    return userRepository
        .findUserDBOByUserIdAndRoles_RoleName(studentId, Role.STUDENT)
        .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
  }

  /**
   * Validates and retrieves a course by its ID and tutor ID.
   *
   * @param courseId the ID of the course
   * @param tutorId the ID of the tutor
   * @return the course entity
   * @throws EntityNotFoundException if the course is not found
   */
  private CourseDBO validateAndGetCourse(Long courseId, Long tutorId) {
    return courseRepository
        .findByCourseIdAndTutor_UserId(courseId, tutorId)
        .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));
  }

  /**
   * Validates and retrieves an address by its ID.
   *
   * @param addressId the ID of the address
   * @return the address entity
   * @throws EntityNotFoundException if the address is not found
   */
  private AddressDBO validateAndGetAddress(Long addressId) {
    return addressRepository
        .findById(addressId)
        .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + addressId));
  }

  /**
   * Checks if a tutor is verified.
   *
   * @param tutorId the ID of the tutor
   * @return true if the tutor is verified, false otherwise
   * @throws EntityNotFoundException if the tutor is not found
   */
  public boolean isTutorVerified(Long tutorId) {
    // Find the user by tutorId
    UserDBO tutor =
        userRepository
            .findById(tutorId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + tutorId));

    // Check if the user has the TUTOR role
    boolean isTutor = tutor.getRoles().stream().anyMatch(role -> role.getRoleName() == Role.TUTOR);

    // If the user is a tutor, check if they are verified
    return isTutor && tutor.getIsVerified() != null && tutor.getIsVerified();
  }
}
