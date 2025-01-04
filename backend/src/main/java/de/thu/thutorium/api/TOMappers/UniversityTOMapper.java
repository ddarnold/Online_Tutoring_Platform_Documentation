package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.UniversityTO;
import de.thu.thutorium.database.dbObjects.UniversityDBO;
import org.mapstruct.Mapper;

/** Mapper interface for converting {@link UniversityDBO} to {@link UniversityTO}. */
@Mapper(componentModel = "spring")
public interface UniversityTOMapper {
  /**
   * Converts a {@link UniversityDBO} object to an {@link UniversityTO} object.
   *
   * @param universityDBO the {@code UniversityDBO} object to convert
   * @return an {@code toDTO} object containing the mapped data
   */
  UniversityTO toDTO(UniversityDBO universityDBO);
}
