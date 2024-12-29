package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.CourseTOMapper;
import de.thu.thutorium.api.transferObjects.CourseTO;
import de.thu.thutorium.database.DBOMappers.CourseDBOMapper;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.CourseRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.exceptions.ResourceNotFoundException;
import de.thu.thutorium.exceptions.SpringErrorPayload;
import de.thu.thutorium.services.interfaces.CourseService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link CourseService} interface that provides various methods for
 * retrieving courses based on different search criteria and getting overall course statistics.
 *
 * <p>This service interacts with the {@link CourseRepository} to fetch course data from the
 * database and uses {@link CourseTOMapper} to convert the {@link CourseDBO} (database object) into
 * {@link CourseTO} (data transfer object) for use in the application.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseDBOMapper courseDBMapper;
    private final CourseTOMapper courseMapper;
    private final UserRepository userRepository;

    /**
     * Finds a course by its unique ID.
     *
     * <p>This method retrieves the course with the given {@code id} from the {@link CourseRepository}
     * and maps it to a {@link CourseTO} object using the {@link CourseTOMapper}.
     *
     * @param id the unique ID of the course to retrieve.
     * @return a {@link CourseTO} representing the course with the given ID
     * @throws ResourceNotFoundException, if the course does not exist in the database.
     */
    @Override
    public CourseTO findCourseById(Long id) {
        Optional<CourseDBO> courseOptional = Optional.ofNullable(courseRepository.findCourseById(id));
        return courseMapper.toDTO(courseOptional.orElseThrow(() -> new ResourceNotFoundException(
                new SpringErrorPayload(
                        "Course not found",
                        "The course with ID " + id + " does not exist in the system.",
                        404
                ).toString()
        )));
    }

    /**
     * Retrieves courses that belong to a specific category.
     *
     * <p>This method retrieves a list of courses that are associated with the specified {@code
     * categoryName}. The result is mapped into a list of {@link CourseTO} objects using the {@link
     * CourseTOMapper}.
     *
     * @param categoryName the name of the category to search for.
     * @return a list of {@link CourseTO} objects representing courses in the specified category. If
     * no courses are found, an empty list is returned.
     */
    @Override
    public List<CourseTO> getCoursesByCategory(String categoryName) {
        List<CourseDBO> courses = courseRepository.findCoursesByCategoryName(categoryName);
        return courses.stream().map(courseMapper::toDTO).toList();
    }

    /**
     * Retrieves the total count of courses in the database.
     *
     * @return the total number of courses
     */
    @Override
    public Long getTotalCountOfCourses() {
        return courseRepository.count(); // Default method from JpaRepository
    }

    /**
     * Creates a new course based on the provided {@link CourseTO}.
     *
     * <p>This method fetches the tutor from the database using the tutor ID from the provided course
     * transfer object (DTO). It then maps the course DTO to a course entity (DBO), sets the tutor and
     * creation timestamp, and saves the new course in the database.
     *
     * @param course the transfer object containing the course data to be created
     * @throws EntityNotFoundException if the tutor with the provided ID is not found
     */
    @Override
    @Transactional
    public CourseTO createCourse(@Valid CourseTO course) {
        //Check if the course already exists
        if (courseRepository.existsByCourseName(course.getCourseName())) {
            throw new EntityExistsException(new SpringErrorPayload(
                    "Course exists",
                    "Course " + course.getCourseName() + " already exists.",
                    409
            ).toString());
        }

        // Create CourseDBO from CourseTO using the mapper
        CourseDBO courseDBO = courseDBMapper.toDBO(course);

        // Set the createdOn timestamp to the current time
        courseDBO.setCreatedOn(LocalDateTime.now());

        // Save the new course entity to the database
        return courseMapper.toDTO(courseRepository.save(courseDBO));
    }

    /**
     * Deletes an existing course by its ID.
     *
     * <p>This method checks if a course with the provided ID exists in the database. If it does, the
     * course is deleted; otherwise, an {@link EntityNotFoundException} is thrown.
     *
     * @param courseId the ID of the course to be deleted
     * @throws EntityNotFoundException if the course with the provided ID does not exist
     */
    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found with ID: " + courseId);
        }
        courseRepository.deleteById(courseId);
    }

    /**
     * Updates the details of an existing course.
     *
     * <p>This method fetches an existing course by its ID, updates its fields based on the provided
     * {@link CourseTO}, and saves the updated course entity in the database. If the course or the
     * tutor is not found, an {@link EntityNotFoundException} is thrown.
     *
     * @param courseId the ID of the course to be updated
     * @param courseTO the transfer object containing the updated course data
     * @throws EntityNotFoundException if the course with the provided ID or the tutor with the
     *                                 provided ID is not found
     */
    @Override
    public void updateCourse(Long courseId, CourseTO courseTO) {
        CourseDBO existingCourse =
                courseRepository
                        .findById(courseId)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Course not found with ID: " + courseId));

        // Update fields of the existing course
        existingCourse.setCourseName(courseTO.getCourseName());
        existingCourse.setDescriptionShort(courseTO.getDescriptionShort());
        existingCourse.setDescriptionLong(courseTO.getDescriptionLong());
        existingCourse.setStartDate(courseTO.getStartDate());
        existingCourse.setEndDate(courseTO.getEndDate());

        // Update associated tutor (use userRepository to get the tutor)
        UserDBO tutor =
                userRepository
                        .findById(courseTO.getTutorId())
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                "Tutor not found with ID: " + courseTO.getTutorId()));
        existingCourse.setTutor(tutor);

        // Save the updated course
        courseRepository.save(existingCourse);
    }
}
