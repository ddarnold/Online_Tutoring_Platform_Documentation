package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.UniversityTO;
import de.thu.thutorium.database.dbObjects.UniversityDBO;
import org.mapstruct.Mapper;

/**
 * A MapStruct mapper interface for converting {@link UniversityTO} to
 * {@link UniversityDBO}.
 */
@Mapper(componentModel = "spring")
public interface UniversityDBOMapper {
    /**
     * Converts an {@link UniversityTO} object to a {@link UniversityDBO}
     * object.
     *
     * @param universityTO the {@code AdminUniversityTO} object to convert
     * @return a {@code UniversityDBO} object containing the mapped data
     */
    UniversityDBO toDBO(UniversityTO universityTO);
}
