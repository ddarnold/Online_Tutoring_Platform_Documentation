package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.UserTOMapper;
import de.thu.thutorium.api.transferObjects.common.UserTO;
import de.thu.thutorium.database.DBOMappers.AffiliationDBOMapper;
import de.thu.thutorium.database.dbObjects.AffiliationDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.AffiliationRepository;
import de.thu.thutorium.database.repositories.CourseRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.exceptions.ResourceAlreadyExistsException;
import de.thu.thutorium.exceptions.SpringErrorPayload;
import de.thu.thutorium.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserTOMapper userMapper;
    private final AffiliationDBOMapper affiliationDBOMapper;
    private final AffiliationRepository affiliationRepository;

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
        return user.map(userMapper::toDTO).orElseThrow(() -> new EntityNotFoundException(new SpringErrorPayload(
                "User does not exist",
                "User with ID " + userId + " does not exist in database.",
                404
        ).toString()));
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
        userRepository.findUserDBOByUserId(userId)
                .ifPresentOrElse(userRepository::delete,
                () -> {
                    throw new EntityNotFoundException("User with ID " + userId + " does not exist in database.");
                }
        );
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
   * @throws UsernameNotFoundException if no user is found with the given ID
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
            throw new UsernameNotFoundException(
                    new SpringErrorPayload(
                            "User name not found",
                            "User not found with id " + id,
                            404)
                            .toString()
            );
        }
    }

    /**
     * Enrolls a user in a course
     *
     * <p>This method enrolls a user in a course, provide both the entities already exist in the database.
     * Checks are additionally provided to check if the user is already enrolled in the course;
     * and that the user has student authorizations,
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
        UserDBO student = userRepository.findUserDBOByUserId(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + studentId + " not found"));

        //Checking if a user is enrolled as a student:
        //Redundant because:
        // - Only a student can access the 'student/**' link to enroll in the course according to the
        // security config settings.
        boolean isStudent = student.getRoles().stream()
                .anyMatch((role) -> role.getRoleName().equals(Role.STUDENT));

        if (!isStudent) {
            throw new IllegalArgumentException("The user is not authorized as a student!");
        }

        // Fetch the course and handle the case where the course is not found
        CourseDBO course = courseRepository.findCourseDBOByCourseId(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + courseId + " not found"));

        // Check if the student is already enrolled in the course
        if (student.getStudentCourses().contains(course)) {
            throw new ResourceAlreadyExistsException("Student with id "
                    + studentId
                    + " is already enrolled in course with id "
                    + courseId);
        }
        student.getStudentCourses().add(course);
        userRepository.save(student);
    }
}
