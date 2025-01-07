package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

/** Transfer object representing the progress of a student in a course. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressTO {

  /** The student associated with the progress. */
  @NotNull(message = "Student ID cannot be null")
  private Long studentId;

  /** The course associated with the progress. */
  @NotNull(message = "Course ID cannot be null")
  private Long courseId;

  /** The points scored by the student in the course. */
  @NotNull(message = "Points cannot be null")
  @PositiveOrZero(message = "Points must be zero or positive")
  private Double points = 0.0;
}
