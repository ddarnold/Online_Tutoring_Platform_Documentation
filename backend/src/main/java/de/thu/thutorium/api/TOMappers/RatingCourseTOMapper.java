package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.RatingCourseTO;
import de.thu.thutorium.database.dbObjects.RatingCourseDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A MapStruct mapper interface for converting between {@link RatingCourseDBO} (Rating Course
 * Database Object) and {@link RatingCourseTO} (Rating Course Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code RatingCourseDBO} (representing a
 * course rating in the database) to a {@code RatingCourseDTO} and vice versa. MapStruct automates
 * the mapping process, providing type-safe and efficient conversion between these two objects.
 *
 * <p>The {@code uses} attribute specifies that the {@link UserTOMapper} will be used to map the
 * {@code student} field of {@link RatingCourseDBO} to {@link de.thu.thutorium.api.transferObjects.UserTO} in the {@code
 * RatingCourseDTO}.
 *
 * <p>Note: The {@code componentModel = "spring"} annotation indicates that MapStruct will generate
 * a Spring bean for this mapper, making it available for dependency injection in Spring components
 * or services.
 */
@Mapper(
    componentModel = "spring",
    uses = {UserTOMapper.class})
public interface RatingCourseTOMapper {
  /**
   * Converts a {@link RatingCourseDBO} (representing a rating given by a student to a course) to a
   * {@link RatingCourseTO}.
   *
   * @param dbo the {@code RatingCourseDBO} object representing the course rating to convert
   * @return a {@code RatingCourseDTO} object containing the course rating data
   */
  @Mapping(target = "student", source = "student") // Map UserDBO to UserBaseDTO
  RatingCourseTO toDTO(RatingCourseDBO dbo);

  /**
   * Converts a {@link RatingCourseTO} (Data Transfer Object representing a rating) to a {@link
   * RatingCourseDBO} (Database Object).
   *
   * <p>This method inherits the inverse configuration from {@link #toDTO(RatingCourseDBO)} but adds
   * an explicit mapping to ignore the {@code course} field in the DBO to avoid circular references.
   *
   * @param dto the {@code RatingCourseDTO} object to convert
   * @return a {@code RatingCourseDBO} object representing the course rating in the database
   */
  @InheritInverseConfiguration
  @Mapping(target = "course", ignore = true) // Explicitly ignore the course field
  RatingCourseDBO toDBO(RatingCourseTO dto);
}
