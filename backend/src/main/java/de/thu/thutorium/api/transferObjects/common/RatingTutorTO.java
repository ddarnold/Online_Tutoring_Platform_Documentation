package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Data Transfer Object (DTO) representing a rating given to a tutor by a student.
 *
 * <p>This class is used to transfer information related to a tutor's rating, including the rating
 * points, review text, the timestamp when the rating was created, and basic information about the
 * student who provided the rating.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingTutorTO {
    /**
     * The unique identifier for the rating.
     *
     * <p>This field represents the unique ID for the rating, which is typically used for identifying
     * and retrieving specific ratings from the system.
     */
    private Long ratingId;

    /**
     * Basic information about the student who gave the rating.
     */
    @NotNull(message = "The student ID cannot be empty")
    private Long studentId;
    private String studentName;

    /**
     * Basic information about the Tutor which received the rating.
     */
    @NotNull(message = "The course ID cannot be empty")
    private Long tutorId;
    private String tutorName;

    /**
     * The rating points given to the tutor.
     *
     * <p>This field stores the numeric value representing the rating points given by the student.
     * Typically, this might be a value between 1 and 10, depending on the rating system.
     */
    @NotNull(message = "Points cannot be null")
    private Double points;

    /**
     * The review text provided by the student.
     *
     * <p>This field contains the feedback or comments written by the student regarding the tutor's
     * performance. It may include details such as the tutor's teaching style, effectiveness, and
     * overall experience.
     */
    private String review;
}
