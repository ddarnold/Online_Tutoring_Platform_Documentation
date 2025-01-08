package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.UserTOMapper;
import de.thu.thutorium.api.transferObjects.common.RatingTutorTO;
import de.thu.thutorium.api.transferObjects.common.UserTO;
import de.thu.thutorium.database.DBOMappers.AffiliationDBOMapper;
import de.thu.thutorium.database.dbObjects.AffiliationDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.RatingTutorDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.AffiliationRepository;
import de.thu.thutorium.database.repositories.CourseRepository;
import de.thu.thutorium.database.repositories.RatingTutorRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface that provides methods for retrieving and
 * managing user data, including retrieving counts of students and tutors, and fetching user details
 * by their unique identifiers.
 *
 * <p>This service interacts with the {@link UserRepository} to retrieve user data and utilizes
 * {@link UserTOMapper} to map data between {@link UserDBO} and {@link UserTO}.
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final UserTOMapper userMapper;
  private final AffiliationDBOMapper affiliationDBOMapper;
  private final AffiliationRepository affiliationRepository;
  private final RatingTutorRepository ratingTutorRepository;

  /**
   * Returns the total number of students in the system.
   *
   * <p>This method queries the {@link UserRepository} to count all users with the role "STUDENT".
   * The count is determined by checking the roles of the users stored in the system.
   *
   * @return the total number of students as a {@code Long}.
   */
  @Override
  public Long getStudentCount() {
    return userRepository.findAll().stream()
        .filter(
            user ->
                user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(Role.STUDENT)))
        .count();
  }

  /**
   * Returns the total number of tutors in the system.
   *
   * <p>This method queries the {@link UserRepository} to count all users with the role "TUTOR". The
   * count is determined by checking the roles of the users stored in the system.
   *
   * @return the total number of tutors as a {@code Long}.
   */
  @Override
  public Long getTutorCount() {
    return userRepository.findAll().stream()
        .filter(
            user ->
                user.getRoles().stream().anyMatch(role -> role.getRoleName().equals(Role.TUTOR)))
        .count();
  }

  /**
   * Finds a user by their unique user ID.
   *
   * <p>This method fetches the {@link UserDBO} object from the {@link UserRepository} using the
   * provided {@code userId} and maps it to a {@link UserTO} using the {@link UserTOMapper}. If no
   * user is found, {@code null} is returned.
   *
   * @param userId the unique ID of the user to retrieve.
   * @return a {@link UserTO} representing the user, or {@code null} if no user is found.
   */
  @Override
  public UserTO findByUserId(Long userId) {
    // Fetch UserDBO from the repository
    Optional<UserDBO> user = userRepository.findUserDBOByUserId(userId);

    // Map UserDBO to UserBaseDTO
    return user.map(userMapper::toDTO)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "User with ID " + userId + " does not exist in database."));
  }

  /**
   * Finds a tutor by their unique tutor ID.
   *
   * <p>This method fetches the {@link UserDBO} object from the {@link UserRepository} using the
   * provided {@code tutorId} and maps it to a {@link UserTO} using the {@link UserTOMapper}. If no
   * tutor is found, {@code null} is returned.
   *
   * @param tutorId the unique ID of the tutor to retrieve.
   * @return a {@link UserTO} representing the tutor, or {@code null} if no tutor is found.
   */
  @Override
  public UserTO getTutorByID(Long tutorId) {
    UserDBO user = userRepository.findByTutorId(tutorId);

    if (user != null) {
      return userMapper.toDTO(user);
    }
    return null;
  }

  /**
   * Deletes a user from the system by their unique user ID.
   *
   * <p>This method fetches the {@link UserDBO} from the {@link UserRepository} using the provided
   * {@code userId}. If the user is found, it is deleted from the system. If the user does not
   * exist, an {@link EntityNotFoundException} is thrown.
   *
   * @param userId the unique ID of the user to delete.
   * @throws EntityNotFoundException if no user is found with the provided {@code userId}.
   */
  @Override
  @Transactional
  public void deleteUser(Long userId) {
    userRepository
        .findUserDBOByUserId(userId)
        .ifPresentOrElse(
            userRepository::delete,
            () -> {
              throw new EntityNotFoundException(
                  "User with ID " + userId + " does not exist in database.");
            });
  }

  /**
   * Updates the details of an existing user in the system.
   *
   * <p>This method retrieves the user by their unique ID, updates their information, including
   * their affiliation, and saves the changes to the database.
   *
   * <p>The affiliation is checked for existence based on the university name and affiliation type.
   * If a matching affiliation is found, it is reused; otherwise, the provided affiliation is saved
   * as a new record.
   *
   * @param id the unique identifier of the user to be updated
   * @param user the {@link UserTO} containing the updated details of the user
   * @return a {@link UserTO} containing the updated user details
   * @throws EntityNotFoundException if no user is found with the given ID
   */
  @Override
  public UserTO updateUser(Long id, UserTO user) {
    Optional<UserDBO> existingUserOptional = userRepository.findById(id);
    if (existingUserOptional.isPresent()) {
      UserDBO existingUser = existingUserOptional.get();

      // Convert AffiliationTO to AffiliationDBO
      AffiliationDBO affiliationDBO = affiliationDBOMapper.toDBO(user.getAffiliation());

      // Check if the affiliation already exists by university name and affiliation type
      Optional<AffiliationDBO> existingAffiliationOptional =
          affiliationRepository.findByAffiliationTypeAndUniversity_UniversityName(
              affiliationDBO.getAffiliationType(),
              affiliationDBO.getUniversity().getUniversityName());

      AffiliationDBO savedAffiliationDBO = existingAffiliationOptional.orElse(affiliationDBO);

      // Set the saved AffiliationDBO in the existing UserDBO
      existingUser.setAffiliation(savedAffiliationDBO);

      // Update other fields of the existing UserDBO
      existingUser.setDescription(user.getDescription());
      existingUser.setEmail(user.getEmail());
      existingUser.setFirstName(user.getFirstName());
      existingUser.setLastName(user.getLastName());

      // Save the updated UserDBO
      UserDBO updatedUser = userRepository.save(existingUser);
      return userMapper.toDTO(updatedUser);
    } else {
      throw new EntityNotFoundException("User with ID " + id + " does not exist in database.");
    }
  }

  /**
   * Enrolls a user in a course
   *
   * <p>This method enrolls a user in a course, provide both the entities already exist in the
   * database. Checks are additionally provided to check if the user is already enrolled in the
   * course; and that the user has student authorizations,
   *
   * @param studentId the unique ID of the student who wants to enroll.
   * @param courseId the course in which the student enrolls.
   * @throws EntityNotFoundException if no user or course is found with the provided parameters.
   * @throws IllegalArgumentException if teh user does not have a STUDENT role.
   */
  @Override
  @Transactional
  public void enrollCourse(Long studentId, Long courseId) {
    // Fetch the student and handle the case where the student is not found
    UserDBO student =
        userRepository
            .findUserDBOByUserId(studentId)
            .orElseThrow(
                () -> new EntityNotFoundException("Student with id " + studentId + " not found"));

    // Checking if a user is enrolled as a student:
    // Redundant because:
    // - Only a student can access the 'student/**' link to enroll in the course according to the
    // security config settings.
    boolean isStudent =
        student.getRoles().stream().anyMatch((role) -> role.getRoleName().equals(Role.STUDENT));

    if (!isStudent) {
      throw new IllegalArgumentException("The user is not authorized as a student!");
    }

    // Fetch the course and handle the case where the course is not found
    CourseDBO course =
        courseRepository
            .findCourseDBOByCourseId(courseId)
            .orElseThrow(
                () -> new EntityNotFoundException("Course with id " + courseId + " not found"));

    // Check if the student is already enrolled in the course
    if (student.getStudentCourses().contains(course)) {
      throw new EntityExistsException(
          "Student with id " + studentId + " is already enrolled in course with id " + courseId);
    }
    student.getStudentCourses().add(course);
    userRepository.save(student);
  }

  /**
   * User rates a tutor.
   *
   * <p>This method allows a student to rate a tutor. If the tutor or the student is not found, an
   * {@link EntityNotFoundException} is thrown.
   *
   * <p>A student can rate a tutor only if he is enrolled in a course offered by the tutor.
   *
   * @param ratingTutorTO the {@link RatingTutorTO} which contains details of the review.
   * @throws EntityNotFoundException if the tutor or the student with the provided ID is not found.
   * @throws IllegalArgumentException if the student is not enrolled in any course offered by the
   *     tutor.
   */
  @Override
  public void rateTutor(RatingTutorTO ratingTutorTO) {

    RatingTutorDBO tutorRating = null;
    Long studentId = ratingTutorTO.getStudentId();
    Long tutorId = ratingTutorTO.getTutorId();
    Double points = ratingTutorTO.getPoints();
    String review = "";
    if (ratingTutorTO.getReview() != null && !ratingTutorTO.getReview().isEmpty()) {
      review = ratingTutorTO.getReview();
    }

    // Fetch the student and handle the case where the student is not found
    UserDBO student =
        userRepository
            .findUserDBOByUserIdAndRoles_RoleName(ratingTutorTO.getStudentId(), Role.STUDENT)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Student with id " + ratingTutorTO.getStudentId() + " not found"));

    // Fetch the tutor and handle the case where the tutor is not found
    UserDBO tutor =
        userRepository
            .findUserDBOByUserIdAndRoles_RoleName(ratingTutorTO.getTutorId(), Role.TUTOR)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Tutor with id " + ratingTutorTO.getTutorId() + " not found"));

    List<CourseDBO> tutorCourses = tutor.getTutorCourses();
    List<CourseDBO> studentCourses = student.getStudentCourses();

    // Check if the student is enrolled to at least on course offered by the tutor
    // in order to be able to review the course
    if (studentCourses.stream().noneMatch(tutorCourses::contains)) {
      throw new IllegalArgumentException(
          "Student with id "
              + student.getUserId()
              + "has not enrolled in any courses offered by"
              + tutor.getUserId());
    }

    // Retrieve existing reviews if any, by the student ID.
    // Since there can be only one review from a student, the retrieved list of reviews can be
    // limited to one.
    List<RatingTutorDBO> tutorRatingDBOExisting =
        ratingTutorRepository.findByTutor_UserIdAndStudent_UserId(tutorId, studentId, Limit.of(1));

    // If there are no existing reviews, create a new review
    if (tutorRatingDBOExisting.isEmpty()) {
      log.info("In empty object");
      tutorRating =
          RatingTutorDBO.builder()
              .student(student)
              .tutor(tutor)
              .review(review)
              .points(points)
              .createdAt(LocalDateTime.now())
              .build();
    } else { // Fetch and update existing reviews
      log.info("In retrieved object");
      tutorRating = tutorRatingDBOExisting.get(0);
      tutorRating.setReview(review);
      tutorRating.setPoints(points);
      tutorRating.setCreatedAt(LocalDateTime.now());
    }
    ratingTutorRepository.save(tutorRating);
  }

  /**
   * A student unenrolls from a course.
   *
   * @param studentId the id of the student who unenrolls.
   * @param courseId the id of the course from which the student unenrolls.
   */
  @Override
  public void unenrollCourse(Long studentId, Long courseId) {
    // Fetch the student from the database
    UserDBO student = userRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

    // Fetch the course from the database
    CourseDBO course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

    // Remove the course from the student's list
    boolean removed = student.getStudentCourses().remove(course);

    // Also remove the student from the course's participants list to maintain bidirectionality
    course.getStudents().remove(student);

    if (!removed) {
      throw new IllegalStateException("Student is not enrolled in the course.");
    }

    // Save the updated entities back to the database
    userRepository.save(student);
    courseRepository.save(course);
  }
}
