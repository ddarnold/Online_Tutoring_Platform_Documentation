package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.RatingCourseTO;
import de.thu.thutorium.database.dbObjects.RatingCourseDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * A MapStruct mapper interface for converting between {@link RatingCourseDBO} (Rating Course
 * Database Object) and {@link RatingCourseTO} (Rating Course Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code RatingCourseDBO} (representing a
 * course rating in the database) to a {@code RatingCourseDTO}. MapStruct automates the mapping
 * process, providing type-safe and efficient conversion between these two objects.
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
  @Mappings({
    @Mapping(target = "studentId", source = "student.userId"), // Map UserDBO to UserBaseDTO
    @Mapping(target = "studentName", source = "student.fullName"), // Map UserDBO to UserBaseDTO
    @Mapping(target = "courseId", source = "course.courseId"), // Map UserDBO to UserBaseDTO
    @Mapping(target = "courseName", source = "course.courseName"), // Map UserDBO to UserBaseDTO
  })
  RatingCourseTO toDTO(RatingCourseDBO dbo);
}
