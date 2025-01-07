package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.RatingTutorTO;
import de.thu.thutorium.database.dbObjects.RatingTutorDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * A MapStruct mapper interface for converting between {@link RatingTutorDBO} (Rating Tutor Database
 * Object) and {@link RatingTutorTO} (Rating Tutor Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code RatingTutorDBO} (representing a
 * tutor rating in the database) to a {@code RatingTutorDTO}. MapStruct is used to automate the
 * mapping process, ensuring type-safe and efficient conversion between these two objects.
 *
 * <p>Note: The {@code componentModel = "spring"} annotation indicates that MapStruct will generate
 * a Spring bean for this mapper, making it available for dependency injection in Spring components
 * or services.
 */
@Mapper(
    componentModel = "spring",
    uses = {UserTOMapper.class})
public interface RatingTutorTOMapper {
  /**
   * Converts a {@link RatingTutorDBO} (representing a rating given by a student to a tutor) to a
   * {@link RatingTutorTO}.
   *
   * @param dbo the {@code RatingTutorDBO} object representing the tutor rating to convert
   * @return a {@code RatingTutorDTO} object containing the tutor rating data
   */
  @Mappings({
    @Mapping(target = "studentId", source = "student.userId"),
    @Mapping(target = "studentName", source = "student.fullName"),
    @Mapping(target = "tutorId", source = "tutor.userId"),
    @Mapping(target = "tutorName", source = "tutor.fullName"),
  })
  RatingTutorTO toDTO(RatingTutorDBO dbo);
}
