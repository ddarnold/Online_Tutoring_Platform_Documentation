package de.thu.thutorium.api.transferObjects;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object (DTO) representing a rating given to a course by a student.
 *
 * <p>This class is used to transfer information related to a course's rating, including the rating
 * points, review text, the timestamp when the rating was created, and basic information about the
 * student who provided the rating.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingCourseTO {
    /**
     * The unique identifier for the rating.
     *
     * <p>This field represents the unique ID for the rating, which is typically used for identifying
     * and retrieving specific ratings from the system.
     */
    @NotNull(message = "The rating id cannot be null")
    private Long ratingId;

    /**
     * The rating points given to the course.
     *
     * <p>This field stores the numeric value representing the rating points given by the student.
     * Typically, this might be a value between 1 and 5, depending on the rating system.
     */
    @NotNull(message = "The points cannot be null")
    private Double points;

    /**
     * The review text provided by the student.
     *
     * <p>This field contains the feedback or comments written by the student regarding the course. It
     * may include details such as the course content, teaching quality, and overall experience.
     */
    private String review;

    /**
     * Basic information about the student who gave the rating.
     *
     * <p>This field contains a {@code UserDTO} object that represents the student who provided
     * the rating. The student's basic information (such as their name) is included in this object.
     */
    private UserTO student;
}
