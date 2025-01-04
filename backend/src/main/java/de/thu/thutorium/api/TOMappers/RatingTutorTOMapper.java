package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.RatingTutorTO;
import de.thu.thutorium.database.dbObjects.RatingTutorDBO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * A MapStruct mapper interface for converting between {@link RatingTutorDBO} (Rating Tutor Database
 * Object) and {@link RatingTutorTO} (Rating Tutor Data Transfer Object).
 *
 * <p>This interface defines the mapping logic to convert a {@code RatingTutorDBO} (representing a
 * tutor rating in the database) to a {@code RatingTutorDTO} and vice versa. MapStruct is used to
 * automate the mapping process, ensuring type-safe and efficient conversion between these two
 * objects.
 *
 * <p>The {@code uses} attribute specifies that the {@link UserTOMapper} will be used to map the
 * {@code student} field of {@link RatingTutorDBO} to {@link de.thu.thutorium.api.transferObjects.common.UserTO} in the {@code
 * RatingTutorDTO}.
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
    @Mapping(target = "student", source = "student") // Map UserDBO to UserBaseDTO
    RatingTutorTO toDTO(RatingTutorDBO dbo);

    /**
     * Converts a {@link RatingTutorTO} (Data Transfer Object representing a rating) to a {@link
     * RatingTutorDBO} (Database Object).
     *
     * <p>This method inherits the inverse configuration from {@link #toDTO(RatingTutorDBO)} but adds
     * additional mappings to avoid circular references.
     *
     * @param dto the {@code RatingTutorDTO} object to convert
     * @return a {@code RatingTutorDBO} object representing the rating in the database
     */
    @InheritInverseConfiguration
    @Mapping(target = "student.givenTutorRatings", ignore = true) // Avoid infinite recursion
    @Mapping(target = "tutor.receivedTutorRatings", ignore = true) // Avoid infinite recursion
    RatingTutorDBO toDBO(RatingTutorTO dto);
}
