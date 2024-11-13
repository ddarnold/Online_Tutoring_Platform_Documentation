package de.thu.thutorium.repository;

import de.thu.thutorium.model.SampleEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link SampleEntity} entities.
 *
 * <p>This interface extends the {@link CrudRepository} interface provided by Spring Data, enabling
 * basic CRUD (Create, Read, Update, Delete) operations on SampleEntity entities.
 *
 * <p>By extending {@link CrudRepository}, this interface inherits several methods for working with
 * SampleEntity persistence, including methods for saving, deleting, and finding SampleEntity
 * instances.
 *
 * @see SampleEntity
 * @see CrudRepository
 */
public interface SampleRepository extends CrudRepository<SampleEntity, Integer> {}
