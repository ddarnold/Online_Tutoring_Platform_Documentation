package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.RatingTutorTO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.RatingTutorDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper interface for converting {@link RatingTutorTO} to {@link RatingTutorDBO}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RatingTutorDBOMapper {
    private final UserRepository userRepository;

    /**
     * Maps a {@link RatingTutorTO} object to a {@link RatingTutorDBO} object.
     *
     * @param tutorRating the {@link RatingTutorTO} object to map.
     * @return the mapped {@link RatingTutorDBO} object.
     */
    public RatingTutorDBO toDBO(RatingTutorTO tutorRating) {

        // Fetch the student and handle the case where the student is not found
        UserDBO student = userRepository.findUserDBOByUserIdAndRoles_RoleName(tutorRating.getStudentId(), Role.STUDENT)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + tutorRating.getStudentId() + " not found"));

        // Fetch the tutor and handle the case where the tutor is not found
        UserDBO tutor = userRepository.findUserDBOByUserIdAndRoles_RoleName(tutorRating.getTutorId(), Role.TUTOR)
                .orElseThrow(() -> new EntityNotFoundException("Tutor with id " + tutorRating.getTutorId() + " not found"));

        List<CourseDBO> tutorCourses = tutor.getTutorCourses();
        List<CourseDBO> studentCourses = student.getStudentCourses();

        //Check if the student is enrolled in at least one course offered by the tutor.
        if (studentCourses.stream().noneMatch(tutorCourses::contains)) {
            throw new IllegalArgumentException("Student with id "
                    + student.getUserId()
                    + "has not enrolled in any courses offered by"
                    + tutor.getUserId());
        }

        //return the DBO
        return RatingTutorDBO.builder()
                .tutor(tutor)
                .student(student)
                .points(tutorRating.getPoints())
                .review(tutorRating.getReview())
                .build();
    }
}
