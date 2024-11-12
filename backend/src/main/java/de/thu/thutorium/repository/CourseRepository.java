package de.thu.thutorium.repository;

import de.thu.thutorium.model.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Integer> {}
