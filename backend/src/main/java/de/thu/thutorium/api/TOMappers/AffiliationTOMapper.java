package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.AffiliationTO;
import de.thu.thutorium.database.dbObjects.AffiliationDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper interface for converting {@link AffiliationDBO} to {@link AffiliationTO}.
 */
@Mapper(componentModel = "spring",
        uses = { UniversityTOMapper.class})
public interface AffiliationTOMapper {
    /**
     * Maps an AffiliationDBO to an AffiliationTO.
     *
     * @param affiliation the source AffiliationDBO
     * @return the resulting AffiliationTO
     */
    @Mappings({
            @Mapping(source = "university.universityName", target = "universityName"),
            @Mapping(source = "affiliationType", target = "affiliationType"),
    })
    AffiliationTO toDTO(AffiliationDBO affiliation);
}
