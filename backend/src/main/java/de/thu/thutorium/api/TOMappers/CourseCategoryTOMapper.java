package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import org.mapstruct.*;

/**
 * A MapStruct mapper interface for converting between {@link CourseCategoryDBO} (Course Category
 * Database Object) and {@link CourseCategoryTO} (Course Category Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code CourseCategoryDBO} (representing
 * a course category in the database) to a {@code CourseCategoryDTO} and vice versa. MapStruct
 * automates the mapping process, ensuring type-safe and efficient conversion between these two
 * objects.
 *
 * <p>The {@code componentModel = "spring"} annotation indicates that MapStruct will generate a
 * Spring bean for this mapper, making it available for dependency injection in Spring components or
 * services.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseCategoryTOMapper {
    /**
     * Converts a {@link CourseCategoryDBO} (representing a course category in the database) to a
     * {@link CourseCategoryTO}.
     *
     * <p>This method maps the {@code categoryName} field of the {@code CourseCategoryDBO} to the
     * {@code categoryName} field in the {@code CourseCategoryDTO}.
     *
     * @param courseCategory the {@code CourseCategoryDBO} object representing the course category to
     *     convert
     * @return a {@code CourseCategoryDTO} object containing the course category data
     */
    @Mapping(source = "categoryName", target = "categoryName")
    CourseCategoryTO toDTO(CourseCategoryDBO courseCategory);

    @Mapping(source = "categoryName", target = "categoryName")
    CourseCategoryDBO toDBO(CourseCategoryTO courseCategoryTO);
}
