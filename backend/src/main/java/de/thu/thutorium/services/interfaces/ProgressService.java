package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.ProgressTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

/**
 * The {@code ProgressService} interface provides methods for managing student progress in courses.
 *
 * <p>It exposes the following functionalities:
 *
 * <ul>
 *   <li>Create progress for a student in a course.
 *   <li>Delete progress for a student in a specific course.
 *   <li>Update progress (e.g., points) for a student in a specific course.
 * </ul>
 */
@Service
public interface ProgressService {
  /**
   * Creates a new progress record for a student in a course.
   *
   * <p>This method stores the student's progress data, such as points, for the specified course. It
   * is typically used when a student starts a course or when initial progress data is available.
   *
   * @param progressTO the {@link ProgressTO} object containing the progress data to be created.
   */
  @Transactional
  void createProgress(ProgressTO progressTO);

  /**
   * Deletes the progress record for a student in a specific course.
   *
   * <p>This method removes the progress record for the specified student and course. If no progress
   * record exists for the provided student and course, the method returns {@code false}.
   *
   * @param studentId the unique ID of the student whose progress is to be deleted.
   * @param courseId the unique ID of the course for which progress is to be deleted.
   * @return {@code true} if the progress record was successfully deleted, {@code false} otherwise.
   */
  boolean deleteProgress(Long studentId, Long courseId);

  /**
   * Updates the progress (e.g., points) for a student in a specific course.
   *
   * <p>This method modifies the student's progress data, updating the points or other relevant
   * information for the specified course. If no existing progress record is found for the student
   * and course, the method may return {@code false}.
   *
   * @param studentId the unique ID of the student whose progress is to be updated.
   * @param courseId the unique ID of the course for which progress is to be updated.
   * @param points the new points or progress data to update for the student in the course.
   * @return {@code true} if the progress record was successfully updated, {@code false} otherwise.
   */
  boolean updateProgress(Long studentId, Long courseId, Double points);
}
