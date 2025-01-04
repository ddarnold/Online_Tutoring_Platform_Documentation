package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.transferObjects.common.ProgressTO;
import de.thu.thutorium.database.DBOMappers.ProgressDBMapper;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.ProgressDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.CourseRepository;
import de.thu.thutorium.database.repositories.ProgressRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.ProgressService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing the progress of students in courses.
 *
 * <p>This service provides methods for creating, updating, and deleting progress records for
 * students in their respective courses. The progress records are stored in the {@link
 * ProgressRepository}, and the service interacts with {@link UserRepository}, {@link
 * CourseRepository}, and {@link ProgressDBMapper} for data retrieval and persistence.
 */
@Service
public class ProgressServiceImpl implements ProgressService {
  private final ProgressRepository progressRepository;
  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final ProgressDBMapper progressDBMapper;

  public ProgressServiceImpl(
      ProgressRepository progressRepository,
      UserRepository userRepository,
      CourseRepository courseRepository,
      ProgressDBMapper progressDBMapper) {
    this.progressRepository = progressRepository;
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
    this.progressDBMapper = progressDBMapper;
  }

  /**
   * Creates a new progress record for a student in a specific course.
   *
   * <p>This method fetches the student and course entities using the provided IDs and creates a new
   * {@link ProgressDBO} entity using the provided {@link ProgressTO}. It then adds the progress
   * record to the student's received scores and saves both the student and the progress record to
   * the database.
   *
   * @param progressTO the transfer object containing the progress details to be created
   * @throws IllegalArgumentException if the student or course cannot be found based on the provided
   *     IDs
   */
  @Override
  @Transactional
  public void createProgress(ProgressTO progressTO) {
    // Fetch the student and course entities
    UserDBO student =
        userRepository
            .findById(progressTO.getStudentId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Invalid student ID: " + progressTO.getStudentId()));

    CourseDBO course =
        courseRepository
            .findById(progressTO.getCourseId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException("Invalid course ID: " + progressTO.getCourseId()));

    // Create ProgressDBO
    ProgressDBO progressDBO = progressDBMapper.toEntity(progressTO, student, course);

    student.getReceivedScores().add(progressDBO);

    userRepository.save(student);
    // Save ProgressDBO
    progressRepository.save(progressDBO);
  }

  /**
   * Deletes a progress record for a student in a specific course.
   *
   * <p>This method searches for the progress record by the provided student and course IDs. If
   * found, the record is deleted from the database. The method returns true if the progress record
   * was successfully deleted, or false if the record was not found.
   *
   * @param userId the ID of the student whose progress is to be deleted
   * @param courseId the ID of the course from which the student's progress will be deleted
   * @return true if the progress record was deleted, false if no record was found for the given IDs
   */
  @Override
  @Transactional
  public boolean deleteProgress(Long userId, Long courseId) {
    // Find the progress record
    ProgressDBO progressDBO = progressRepository.findByUserIdAndCourseId(userId, courseId);
    if (progressDBO != null) {
      // Delete the progress record
      progressRepository.delete(progressDBO);
      return true;
    }
    return false;
  }

  /**
   * Updates the progress record for a student in a specific course with new points.
   *
   * <p>This method searches for the progress record by the provided student and course IDs. If
   * found, it updates the points in the progress record and saves the updated record to the
   * database. The method returns true if the progress record was successfully updated, or false if
   * the record was not found.
   *
   * @param userId the ID of the student whose progress is to be updated
   * @param courseId the ID of the course for which the progress is to be updated
   * @param points the new points to be updated in the progress record
   * @return true if the progress record was updated, false if no record was found for the given IDs
   */
  @Override
  public boolean updateProgress(Long userId, Long courseId, Double points) {
    // Find the progress record
    ProgressDBO progressDBO = progressRepository.findByUserIdAndCourseId(userId, courseId);
    if (progressDBO != null) {
      // Update the points
      progressDBO.setPoints(points);
      // Save the updated progress record
      progressRepository.save(progressDBO);
      return true;
    }
    return false;
  }
}
