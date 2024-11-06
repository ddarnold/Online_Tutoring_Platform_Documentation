package de.thu.thutorium;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface SampleRepository extends CrudRepository<SampleEntity, Integer> {
}
