package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.TutorTO;

import java.util.List;

/**
 * The {@code SearchService} interface provides methods for searching tutors, courses, and
 * retrieving available categories.
 *
 * <p>It exposes the following functionalities:
 *
 * <ul>
 *   <li>Search tutors by name.
 *   <li>Search courses by name.
 *   <li>Retrieve all available course categories.
 * </ul>
 */
public interface SearchService {
  /**
   * Searches for tutors by the given tutor's name.
   *
   * <p>This method will return a list of {@link de.thu.thutorium.api.transferObjects.common.UserTO}
   * objects that match the given tutor name. The search may be case-insensitive and can return
   * partial matches depending on the implementation.
   *
   * @param tutorName the name (or partial name) of the tutor to search for.
   * @return a list of {@link de.thu.thutorium.api.transferObjects.common.UserTO} objects
   *     representing tutors that match the search criteria. If no tutors are found, an empty list
   *     is returned.
   */
  List<TutorTO> searchTutors(String tutorName);

}
