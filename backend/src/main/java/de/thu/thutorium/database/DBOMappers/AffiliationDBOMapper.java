package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.AffiliationTO;
import de.thu.thutorium.database.dbObjects.AffiliationDBO;
import de.thu.thutorium.database.dbObjects.UniversityDBO;
import de.thu.thutorium.database.dbObjects.enums.AffiliationType;
import de.thu.thutorium.database.repositories.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

/**
 * A mapper class for converting {@link AffiliationTO} to
 * {@link AffiliationDBO}.
 *
 */
@Component
@RequiredArgsConstructor
public class AffiliationDBOMapper {
    private final UniversityRepository universityRepository;

    /**
     * Converts an {@link AffiliationTO} object to a {@link AffiliationDBO}
     * object.
     *
     * @param affiliation the {@code AdminUniversityTO} object to convert
     * @return a {@code toDBO} object containing the mapped data
     */
    public AffiliationDBO toDBO(AffiliationTO affiliation) {
        AffiliationDBO affiliationDBO = new AffiliationDBO();
        affiliationDBO.setAffiliationType(AffiliationType.valueOf(affiliation.getAffiliationType()));
        affiliationDBO.setAffiliatedUsers(new ArrayList<>());
        Optional<UniversityDBO> universityDBO = universityRepository.findByUniversityName(affiliation.getUniversityName());
        if (universityDBO.isPresent()) {
            affiliationDBO.setUniversity(universityDBO.get());
        } else {
            throw new IllegalArgumentException("University not found: " + affiliation.getUniversityName());
        }
        return affiliationDBO;
    }
}
