package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.MeetingDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link MeetingDBO} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD (Create, Read, Update, Delete)
 * operations and query methods for {@link MeetingDBO} entities, which represent a meeting in the
 * system.
 *
 * <p>The repository is annotated with {@link Repository}, indicating that it is a Spring Data JPA
 * repository and should be managed by the Spring container.
 *
 * <p>Custom query methods can be added here if needed, leveraging Spring Data JPA's query creation
 * mechanism.
 */
@Repository
public interface MeetingRepository extends JpaRepository<MeetingDBO, Long> {

  /**
   * Finds meetings in which a user is a participant.
   *
   * <p>This query joins the {@link MeetingDBO} entity with its participants and retrieves all
   * meetings where the provided user ID matches a participant's user ID.
   *
   * @param userId the unique identifier of the user whose participated meetings are to be retrieved
   * @return a list of {@link MeetingDBO} entities representing meetings the user has participated
   *     in
   */
  @Query("SELECT m FROM MeetingDBO m JOIN m.participants p WHERE p.userId = :userId")
  List<MeetingDBO> findParticipatedMeetingsByUserId(@Param("userId") Long userId);

  /**
   * Finds meetings scheduled by a specific tutor.
   *
   * <p>This query retrieves all meetings where the provided user ID matches the tutor's user ID. It
   * leverages the one-to-many relationship between tutors and their scheduled meetings.
   *
   * @param userId the unique identifier of the tutor whose scheduled meetings are to be retrieved
   * @return a list of {@link MeetingDBO} entities representing meetings scheduled by the tutor
   */
  @Query("SELECT m FROM MeetingDBO m WHERE m.tutor.userId = :userId")
  List<MeetingDBO> findScheduledMeetingsByTutorId(@Param("userId") Long userId);
}
