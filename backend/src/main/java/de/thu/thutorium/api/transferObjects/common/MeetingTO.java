package de.thu.thutorium.api.transferObjects.common;

import de.thu.thutorium.database.dbObjects.enums.MeetingStatus;
import de.thu.thutorium.database.dbObjects.enums.MeetingType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingTO {
    /** The tutor who created the meeting. */
    @NotNull(message = "Tutor cannot be null")
    private Long tutorId;

    /** The course to which this meeting is related. */
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    /** The date of the meeting. */
    @NotNull(message = "Meeting date cannot be null")
    private LocalDate meetingDate;

    /** The time of the meeting. */
    @NotNull(message = "Meeting time cannot be null")
    private LocalDateTime meetingTime;

    /** The time of the meeting. */
    @NotNull(message = "Meeting end time cannot be null")
    private LocalDateTime endTime;

    /** The duration of the meeting in minutes. */
    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    /** The types of the meeting. */
    @NotNull(message = "Meeting types cannot be null")
    private MeetingType meetingType;

    /** The status of the meeting. */
    @NotNull(message = "Meeting status cannot be null")
    private MeetingStatus meetingStatus;

    /** The address ID where the meeting is being held. */
    @NotNull(message = "Address ID cannot be null")
    private Long addressId;

    @NotEmpty(message = "Room number cant be empty")
    private String roomNum;

    @NotEmpty(message = "Participants cannot be empty")
    private List<Long> participantIds;
}
