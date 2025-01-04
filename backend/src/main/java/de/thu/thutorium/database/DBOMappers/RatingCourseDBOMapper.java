package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.RatingCourseTO;
import de.thu.thutorium.database.dbObjects.*;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.CourseRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mapper interface for converting {@link RatingCourseTO} to {@link RatingCourseDBO}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RatingCourseDBOMapper {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Maps a {@link RatingCourseTO} object to a {@link RatingCourseDBO} object.
     *
     * @param courseRating the {@link RatingCourseTO} object to map.
     * @return the mapped {@link RatingCourseDBO} object.
     */
    public RatingCourseDBO toDBO(RatingCourseTO courseRating) {

        // Fetch the student and handle the case where the student is not found
        UserDBO student = userRepository.findUserDBOByUserIdAndRoles_RoleName(courseRating.getStudentId(), Role.STUDENT)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + courseRating.getStudentId() + " not found"));

        // Fetch the course and handle the case where the course is not found
        CourseDBO course = courseRepository.findCourseDBOByCourseId(courseRating.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course with id " + courseRating.getCourseId() + " not found"));

        // Check if the student is enrolled in the course
        if (!student.getStudentCourses().contains(course)) {
            throw new EntityNotFoundException("Student with id "
                    + courseRating.getStudentId()
                    + " is not enrolled in course with id "
                    + courseRating.getCourseId());
        }

        return RatingCourseDBO.builder()
                .course(course)
                .student(student)
                .points(courseRating.getPoints())
                .review(courseRating.getReview())
                .build();
    }
}
