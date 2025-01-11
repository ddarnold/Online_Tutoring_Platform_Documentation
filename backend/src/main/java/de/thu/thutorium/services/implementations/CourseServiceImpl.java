package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.CourseTOMapper;
import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.RatingCourseTO;
import de.thu.thutorium.database.DBOMappers.CourseDBOMapper;
import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.RatingCourseDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.*;
import de.thu.thutorium.exceptions.ResourceAlreadyExistsException;
import de.thu.thutorium.services.interfaces.CourseService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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
  private final CategoryRepository categoryRepository;
  private final CourseDBOMapper courseDBMapper;
  private final CourseTOMapper courseMapper;
  private final UserRepository userRepository;
  private final RatingCourseRepository ratingCourseRepository;
  private final RatingTutorRepository ratingTutorRepository;

  /**
   * Finds a course by its unique ID.
   *
   * <p>This method retrieves the course with the given {@code id} from the {@link CourseRepository}
   * and maps it to a {@link CourseTO} object using the {@link CourseTOMapper}.
   *
   * @param id the unique ID of the course to retrieve.
   * @return a {@link CourseTO} representing the course with the given ID
   * @throws EntityNotFoundException, if the course does not exist in the database.
   */
  @Override
  public CourseTO findCourseById(Long id) {
    Optional<CourseDBO> courseOptional = courseRepository.findCourseDBOByCourseId(id);
    return courseMapper.toDTO(
        courseOptional.orElseThrow(
            () ->
                new EntityNotFoundException(
                    "The course with ID " + id + " does not exist in the system.")));
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
   *     no courses are found, an empty list is returned.
   */
  @Override
  public List<CourseTO> getCoursesByCategory(String categoryName) {
    List<CourseDBO> courses = courseRepository.findCoursesByCategoryName(categoryName);
    return courses.stream().map(courseMapper::toDTO).toList();
  }

  @Override
  public List<CourseTO> getCourseByTutorId(Long tutorId) {
    List<CourseDBO> courseDBOs = courseRepository.findByTutor_UserId(tutorId);
    return courseMapper.toDTOList(courseDBOs);
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
    // Check if the course already exists
    if (courseRepository.existsByCourseName(course.getCourseName())) {
      throw new EntityExistsException("Course " + course.getCourseName() + " already exists.");
    }

    // Create CourseDBO from CourseTO using the mapper
    CourseDBO courseDBO = courseDBMapper.toDBO(course);

    // Update the course categories resolution table from the owning side (category)
    courseDBO
        .getCourseCategories()
        .forEach(
            category -> {
              category.getCourses().add(courseDBO);
            });

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
   * @return
   * @throws EntityNotFoundException if the course with the provided ID or the tutor with the
   *     provided ID is not found
   */
  @Override
  @Transactional
  public CourseTO updateCourse(Long courseId, CourseTO courseTO) {
    CourseDBO existingCourse =
        courseRepository
            .findById(courseId)
            .orElseThrow(
                () -> new EntityNotFoundException("Course not found with ID: " + courseId));

    // All validity checks (eg: tutor, categories etc.) will be performed in the DBOmapper
    CourseDBO newCourse = courseDBMapper.toDBO(courseTO);

    // Fetch the new course categories
    List<CourseCategoryDBO> newCategories = new ArrayList<>(newCourse.getCourseCategories());

    // Clear the existing categories
    existingCourse
        .getCourseCategories()
        .forEach(category -> category.getCourses().remove(existingCourse));
    existingCourse.getCourseCategories().clear();

    // Set the new categories
    newCategories.forEach(category -> category.getCourses().add(existingCourse));
    existingCourse.setCourseCategories(newCategories);

    // Update the existing course with the new course details
    existingCourse.setCourseName(newCourse.getCourseName());
    existingCourse.setTutor(newCourse.getTutor());
    existingCourse.setDescriptionShort(newCourse.getDescriptionShort());
    existingCourse.setDescriptionLong(newCourse.getDescriptionLong());
    existingCourse.setStartDate(newCourse.getStartDate());
    existingCourse.setEndDate(newCourse.getEndDate());
    existingCourse.setCourseCategories(newCategories);
    existingCourse.setCreatedOn(LocalDateTime.now());

    // Save the updated course
    CourseDBO savedCourse = courseRepository.save(existingCourse);
    return courseMapper.toDTO(savedCourse);
  }

  /**
   * User rates an existing course.
   *
   * <p>This method allows a student to rate an existing course. If the course or the student is not
   * found, an {@link EntityNotFoundException} is thrown.
   *
   * @param ratingCourseTO the {@link RatingCourseTO} which contains details of the review.
   * @throws EntityNotFoundException if the course with the provided ID or the student with the
   *     provided ID is not found
   */
  @Override
  public void rateCourse(RatingCourseTO ratingCourseTO) {
    RatingCourseDBO courseRating = null;
    Long studentId = ratingCourseTO.getStudentId();
    Long courseId = ratingCourseTO.getCourseId();
    Double points = ratingCourseTO.getPoints();
    String review = "";
    if (ratingCourseTO.getReview() != null && !ratingCourseTO.getReview().isEmpty()) {
      review = ratingCourseTO.getReview();
    }

    // Fetch the student and handle the case where the student is not found
    UserDBO student =
        userRepository
            .findUserDBOByUserId(studentId)
            .orElseThrow(
                () -> new EntityNotFoundException("Student with id " + studentId + " not found"));

    // Checking if a user is enrolled as a student:
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
    if (!student.getStudentCourses().contains(course)) {
      throw new ResourceAlreadyExistsException(
          "Student with id " + studentId + " is not enrolled in course with id " + courseId);
    }

    List<RatingCourseDBO> courseRatingDBOExisting =
        ratingCourseRepository.findByCourse_CourseIdAndStudent_UserId(
            courseId, studentId, Limit.of(1));

    if (courseRatingDBOExisting.isEmpty()) {
      courseRating =
          RatingCourseDBO.builder()
              .course(course)
              .student(student)
              .review(review)
              .points(points)
              .createdAt(LocalDateTime.now())
              .build();
    } else {
      courseRating = courseRatingDBOExisting.get(0);
      courseRating.setReview(review);
      courseRating.setPoints(points);
      courseRating.setCreatedAt(LocalDateTime.now());
    }
    ratingCourseRepository.save(courseRating);
  }

  /**
   * Searches for courses based on the course name.
   *
   * <p>This method fetches the list of courses whose names match the given {@code courseName}. The
   * search may support partial matches depending on the implementation. The result is mapped into a
   * list of {@link CourseTO} objects.
   *
   * @param courseName the name of the course (can be partial).
   * @return a list of {@link CourseTO} objects representing the courses that match the search
   *     criteria. If no courses are found, an empty list is returned.
   */
  @Override
  public List<CourseTO> searchCourses(String courseName) {
    List<CourseDBO> courses = courseRepository.findCourseByName(courseName);
    return courses.stream().map(courseMapper::toDTO).toList();
  }
}
