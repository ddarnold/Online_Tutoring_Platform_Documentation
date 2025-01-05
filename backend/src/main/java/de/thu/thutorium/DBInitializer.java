package de.thu.thutorium;

import de.thu.thutorium.database.dbObjects.RoleDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/** This class initializes the database with default roles and schema constraints. */
@Transactional
@Component
@Slf4j
public class DBInitializer {
  private final RoleRepository roleRepository;
  private final JdbcTemplate jdbcTemplate;

  /**
   * Constructor for DBInitializer.
   *
   * @param roleRepository the repository for role data access
   * @param jdbcTemplate the JdbcTemplate to execute SQL queries
   */
  @Autowired
  public DBInitializer(RoleRepository roleRepository, JdbcTemplate jdbcTemplate) {
    this.roleRepository = roleRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * Initializes the database with default roles and schema constraints. This method is called after
   * the bean's properties have been set.
   */
  @PostConstruct
  public void init() {
    // Initialize roles if they don't exist
    initializeRoles();

    // Add database constraints if not already present
    addDatabaseConstraints();
  }

  /** Initializes roles in the database if they don't already exist.
   *
   *
   *
   * @Author Jossin Anthony
   * */
  private void initializeRoles() {
    if (roleRepository.existsByRoleName(Role.ADMIN)
        && roleRepository.existsByRoleName(Role.VERIFIER)
        && roleRepository.existsByRoleName(Role.TUTOR)
        && roleRepository.existsByRoleName(Role.STUDENT)) {
      return; // Exit if the roles already exist
    } else {
      // Create roles if they don't exist
      RoleDBO adminRole = new RoleDBO(Role.ADMIN);
      RoleDBO studentRole = new RoleDBO(Role.STUDENT);
      RoleDBO tutorRole = new RoleDBO(Role.TUTOR);
      RoleDBO verifierRole = new RoleDBO(Role.VERIFIER);

      roleRepository.save(adminRole);
      roleRepository.save(studentRole);
      roleRepository.save(tutorRole);
      roleRepository.save(verifierRole);
    }
  }

  /**
   * @Author Nikolai Ivanov (Kekschorstviy)
   * //Todo: The constraints work in most cases, but it was possible to create a meeting in the same
   * room within an occupied timerange by another tutor.
   * - Please check for all cases, including but not limited to:
   * - A meeting should not be created WITHIN an occupied time range at a particular location, irrespective
   * of if it is from the same or a different tutor.
   * - The same tutor cannot have overlapping sessions.
   * - Check start and end times on different days (edge cases)
   * - Set an upper limit to possible dates (within an year etc.)?
   * Explore alternate methods (only if it makes sense):
   * ChatGPT example->
   *
   *   boolean isOverlapping = meetingRepository.existsByRoomNumberAndMeetingTypeAndTimeRangeOverlap(
   *     roomNumber, meetingType, meetingTime, meetingEndTime
   * );
   * if (isOverlapping) {
   *     throw new IllegalArgumentException("Meeting overlaps with an existing meeting.");
   * }

   * @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
   *        "FROM Meeting m " +
   *        "WHERE m.roomNumber = :roomNumber " +
   *        "AND m.meetingType = :meetingType " +
   *        "AND m.meetingTime < :endTime AND m.meetingEndTime > :startTime")
   * boolean existsByRoomNumberAndMeetingTypeAndTimeRangeOverlap(
   *     @Param("roomNumber") int roomNumber,
   *     @Param("meetingType") String meetingType,
   *     @Param("startTime") LocalDateTime startTime,
   *     @Param("endTime") LocalDateTime endTime
   * );
   * */
  private String addDatabaseConstraints() {
    try {
      // Step 1: Create extension
      jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS btree_gist");

      // Step 2: Drop the column if it already exists
      jdbcTemplate.execute(
              "ALTER TABLE meeting DROP COLUMN IF EXISTS time_range"
      );

      // Step 3: Recreate the column as GENERATED ALWAYS
      jdbcTemplate.execute(
              "ALTER TABLE meeting "
                      + "ADD COLUMN time_range tsrange GENERATED ALWAYS AS (tsrange(meeting_time, meeting_end_time)) STORED"
      );

      // Step 4: Add constraints for overlapping meetings
      jdbcTemplate.execute(
              "ALTER TABLE meeting "
                      + "ADD CONSTRAINT no_overlapping_meetings "
                      + "EXCLUDE USING gist (time_range WITH &&, room_number WITH =, address_id WITH =) "
                      + "WHERE (meeting_type = 'OFFLINE')"
      );

      jdbcTemplate.execute(
              "ALTER TABLE meeting "
                      + "ADD CONSTRAINT no_tutor_overlapping_meetings "
                      + "EXCLUDE USING gist (time_range WITH &&, created_by WITH =) "
                      + "WHERE (meeting_type IN ('OFFLINE', 'HYBRID', 'ONLINE'))"
      );

      log.info("Database constraints added successfully.");
      return "Database constraints added successfully.";
    } catch (Exception e) {
      log.error("Error adding database constraints: " + e.getMessage());
      return e.getMessage();
    }
  }

}
