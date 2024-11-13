package de.thu.thutorium.service;

import de.thu.thutorium.model.SampleEntity;
import de.thu.thutorium.repository.SampleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to {@link SampleEntity} objects.
 *
 * <p>This service provides methods to interact with the {@link SampleRepository} and perform
 * operations on SampleEntity objects.
 *
 * <p>The {@code SampleService} class is annotated with {@link Service}, indicating that it's a
 * service component in the Spring framework, which makes it eligible for component scanning and
 * auto-detection.
 *
 * @see SampleEntity
 * @see SampleRepository
 */
@Service
public class SampleService {

  /** Repository for performing CRUD operations on {@link SampleEntity} entities. */
  @Autowired private SampleRepository sampleRepository;

  /**
   * Retrieves a list of all {@link SampleEntity} objects from the repository.
   *
   * @return a list containing all SampleEntity instances.
   */
  public List<SampleEntity> allEntities() {
    return (List<SampleEntity>) sampleRepository.findAll();
  }
}
