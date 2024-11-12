package de.thu.thutorium.repository;

import de.thu.thutorium.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {}
