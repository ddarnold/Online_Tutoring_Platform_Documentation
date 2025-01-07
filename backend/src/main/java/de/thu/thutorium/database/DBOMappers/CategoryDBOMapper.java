package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/** A mapper class for converting {@link CourseCategoryTO} to {@link CourseCategoryDBO}. */
@Component
public class CategoryDBOMapper {
  /** Converts an {@link CourseCategoryTO} object to an {@link CourseCategoryDBO} object. */
  public CourseCategoryDBO toDBO(CourseCategoryTO courseCategory) {
    CourseCategoryDBO courseCategoryDBO = new CourseCategoryDBO();
    courseCategoryDBO.setCategoryName(courseCategory.getCategoryName());
    courseCategoryDBO.setCreatedOn(LocalDateTime.now());
    return courseCategoryDBO;
  }
}
