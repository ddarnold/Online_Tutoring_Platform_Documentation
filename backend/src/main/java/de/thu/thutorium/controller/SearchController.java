package de.thu.thutorium.controller;

import de.thu.thutorium.service.SearchService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

  @Autowired private SearchService searchService;

  /**
   * Searches for tutors or courses based on the provided query parameters.
   *
   * <p>This endpoint supports searching for tutors by name and courses by name. If both parameters
   * are provided, the search will return combined results of matching tutors and courses.
   *
   * @param tutorName Optional. The name of the tutor to search for. If null or empty, tutor search
   *     is skipped.
   * @param courseName Optional. The name of the course to search for. If null or empty, course
   *     search is skipped.
   * @return A list of search results containing either tutors, courses, or both, depending on the
   *     provided parameters. Results may include duplicates if multiple entities match the search
   *     criteria.
   */
  @GetMapping
  public List<Object> search(
      @RequestParam(required = false) String tutorName,
      @RequestParam(required = false) String courseName) {
    // Initialize an empty list to store results
    List<Object> results = new ArrayList<>();

    // If tutorName is provided, search for tutors and add to the results
    if (tutorName != null && !tutorName.isEmpty()) {
      results.addAll(searchService.searchTutors(tutorName)); // Add tutors to the results list
    }

    // If courseName is provided, search for courses and add to the results
    if (courseName != null && !courseName.isEmpty()) {
      results.addAll(searchService.searchCourses(courseName)); // Add courses to the results list
    }

    // Return the combined results without removing duplicates
    return results;
  }
}
