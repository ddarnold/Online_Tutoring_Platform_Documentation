package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.UniversityTO;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing universities. This interface defines methods for creating and
 * managing university entities.
 */
@Service
public interface UniversityService {
  /**
   * Creates a new university.
   *
   * @param university the {@code UniversityTO} object containing the university data
   * @return the created {@code UniversityTO} object
   */
  UniversityTO createUniversity(UniversityTO university);

  /**
   * Checks if the university already exists.
   *
   * @param university the {@code UniversityTO} object containing the university data
   * @return the boolean value corresponding to if the university already exists or not.
   */
  Boolean universityExists(UniversityTO university);
}
