package de.thu.thutorium.repository;

import de.thu.thutorium.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing {@link User} entities.
 *
 * <p>This interface extends the {@link CrudRepository} interface provided by Spring Data, which
 * allows for basic CRUD (Create, Read, Update, Delete) operations on User entities.
 *
 * <p>By extending {@link CrudRepository}, the {@code UserRepository} gains several methods for
 * working with User persistence, including methods for saving, deleting, and finding User
 * instances.
 *
 * @see User
 * @see CrudRepository
 */
public interface UserRepository extends CrudRepository<User, Integer> {}
