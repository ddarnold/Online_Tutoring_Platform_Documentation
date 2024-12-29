package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.MeetingDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    List<MeetingDBO> findByMeetingDateAndRoomNumAndMeetingTimeLessThanEqualAndMeetingTimeGreaterThanEqual(
            LocalDate meetingDate,
            String roomNum,
            LocalDateTime newMeetingEnd,
            LocalDateTime newMeetingStart
    );
}
