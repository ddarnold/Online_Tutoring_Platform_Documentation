package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.ChatDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link ChatDBO} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD (Create, Read, Update, Delete)
 * operations and query methods for {@link ChatDBO} entities, which represent a chat in the system.
 *
 * <p>The repository is annotated with {@link Repository}, indicating that it is a Spring Data JPA
 * repository and should be managed by the Spring container.
 *
 * <p>Custom query methods can be added here if needed, leveraging Spring Data JPA's query creation
 * mechanism.
 */
@Repository
public interface ChatRepository extends JpaRepository<ChatDBO, Long> {
  List<ChatDBO> findByParticipants_UserId(Long userId);
}
