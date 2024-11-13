package de.thu.thutorium.controller;

import de.thu.thutorium.model.SampleEntity;
import de.thu.thutorium.service.SampleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling HTTP requests related to sample entities.
 *
 * <p>This controller provides endpoints for managing and retrieving sample entities. It maps to the
 * "/sample" URL path.
 */
@RestController
@RequestMapping("/sample")
public final class SampleController {

  /** Service layer for handling business logic related to sample entities. */
  @Autowired private SampleService sampleService;

  /**
   * Retrieves all sample entities.
   *
   * <p>This endpoint allows clients to fetch a list of all available sample entities. It supports
   * Cross-Origin Resource Sharing (CORS) for requests from "<a
   * href="http://localhost:3000">localhost:3000</a>" with a maximum age of 3600 seconds.
   *
   * @return a {@link ResponseEntity} containing a list of {@link SampleEntity} objects and an HTTP
   *     status of OK.
   */
  @GetMapping
  @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
  public ResponseEntity<List<SampleEntity>> getAllSamples() {
    return new ResponseEntity<>(sampleService.allEntities(), HttpStatus.OK);
  }
}
