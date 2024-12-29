package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.UserTOMapper;
import de.thu.thutorium.api.transferObjects.UserTO;
import de.thu.thutorium.database.DBOMappers.AffiliationDBOMapper;
import de.thu.thutorium.database.dbObjects.AffiliationDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.AffiliationRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.exceptions.SpringErrorPayload;
import de.thu.thutorium.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTOMapper userMapper;
    private final AffiliationDBOMapper affiliationDBOMapper;
    private final AffiliationRepository affiliationRepository;

    /**
     * Constructs a new instance of {@link UserServiceImpl}.
     *
     * @param userRepository the {@link UserRepository} instance used to access user data
     * @param userMapper     the {@link UserTOMapper} instance used for mapping between database and
     *                       transfer objects
     */
    public UserServiceImpl(
            UserRepository userRepository,
            UserTOMapper userMapper,
            AffiliationDBOMapper affiliationDBOMapper,
            AffiliationRepository affiliationRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.affiliationDBOMapper = affiliationDBOMapper;
        this.affiliationRepository = affiliationRepository;
    }

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
        // Check if the user exists before attempting to delete
        UserDBO user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(
                        new SpringErrorPayload(
                                "Username not found",
                                "User not found with ID: " + userId,
                                404)
                                .toString()
                )
        );

        // Delete the user from the repository
        userRepository.delete(user);
    }

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
}
