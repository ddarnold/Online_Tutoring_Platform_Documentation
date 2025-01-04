package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import org.mapstruct.*;

import java.util.List;

/**
 * A MapStruct mapper interface for converting between {@link CourseDBO} (Course Database Object)
 * and {@link CourseTO} (Course Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code CourseDBO} (representing a course
 * in the database) to a {@code CourseDTO} and vice versa. MapStruct automates the mapping process,
 * ensuring type-safe and efficient conversion between these two objects.
 *
 * <p>The {@code uses} attribute specifies that the {@link UserTOMapper} and {@link
 * CourseCategoryTOMapper} will be used to map the {@code createdBy} field (which is a {@code
 * UserDBO}) to a {@code UserBaseDTO} in the {@code CourseDTO}, and the {@code category} field
 * (which is a {@code CourseCategoryDBO}) to a {@code CourseCategoryDTO} in the {@code CourseDTO}.
 *
 * <p>Note: The {@code componentModel = "spring"} annotation indicates that MapStruct will generate
 * a Spring bean for this mapper, making it available for dependency injection in Spring components
 * or services.
 */
@Mapper(
    componentModel = "spring",
    uses = {UserTOMapper.class, CourseCategoryTOMapper.class})
public interface CourseTOMapper {

  /**
   * Maps a single CourseDBO to a CourseDTO.
   *
   * @param course The CourseDBO instance to map.
   * @return The mapped CourseDTO.
   */
  // Map received course ratings
  @Mappings({
          @Mapping(target = "tutorId", source = "tutor.userId"), // Map tutor ID
          @Mapping(target = "tutorName", source = "tutor.fullName") // Map tutor name,

  })

  CourseTO toDTO(CourseDBO course);

  /**
   * Maps a list of CourseDBO instances to a list of CourseDTOs.
   *
   * @param courses The list of CourseDBO instances to map.
   * @return The mapped list of CourseDTOs.
   */
  List<CourseTO> toDTOList(List<CourseDBO> courses);
}
