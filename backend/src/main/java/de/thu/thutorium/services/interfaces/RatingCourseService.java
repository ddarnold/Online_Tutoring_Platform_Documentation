package de.thu.thutorium.services.interfaces;
import de.thu.thutorium.api.transferObjects.common.RatingCourseTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingCourseService {
    List<RatingCourseTO> getCourseRatings(Long courseId);
}