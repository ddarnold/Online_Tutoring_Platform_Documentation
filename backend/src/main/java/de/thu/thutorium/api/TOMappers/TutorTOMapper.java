package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.TutorTO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A MapStruct mapper interface for converting between {@link UserDBO} (User Database Object) and
 * {@link TutorTO} (Tutor Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code UserDBO} (representing a tutor)
 * to a {@code TutorTO}. It uses MapStruct to automate the conversion process, making the mapping
 * type-safe and efficient.
 *
 * <p>The {@code uses} attribute specifies other mappers that will be used for mapping related
 * objects, including: {@link CourseTOMapper} for mapping courses associated with the tutor
 *
 * <p>Note: The {@code componentModel = "spring"} annotation indicates that MapStruct will generate
 * a Spring bean for this mapper, allowing it to be injected into Spring components or services.
 */
@Mapper(
    componentModel = "spring",
    uses = { CourseTOMapper.class }
)
public interface TutorTOMapper {
  /**
   * Converts a {@link UserDBO} (representing a tutor) to a {@link TutorTO}.
   *
   * @param tutor the {@code UserDBO} object representing the tutor to convert
   * @return a {@code TutorTO} object containing the tutor's data
   */
  @Mapping(
      target = "tutorCourses", source = "tutorCourses") // Map Set<CourseDBO> to List<CourseDTO>
  TutorTO toDTO(UserDBO tutor);

//  List<TutorTO> toDTOList(List<UserDBO> users);
}
