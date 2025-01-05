package de.thu.thutorium.api.transferObjects.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.thu.thutorium.database.dbObjects.enums.MeetingStatus;
import de.thu.thutorium.database.dbObjects.enums.MeetingType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class MeetingTO {
    /** The tutor who created the meeting. */
    @NotNull(message = "Tutor cannot be null")
    private Long tutorId;

    private String tutorName;

    /** The course to which this meeting is related. */
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    private String courseName;

    //Todo: Review -> Check DB comments
//    /** The date of the meeting. */
//    @NotNull(message = "Meeting date cannot be null")
//    @FutureOrPresent(message = "The meeting date must be in the present or future.")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private LocalDate meetingDate;

    /** The time of the meeting. */
    @NotNull(message = "Meeting time cannot be null")
    @FutureOrPresent(message = "The meeting start time must be in the present or future.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime meetingTime;

    /** The time of the meeting. */
    @NotNull(message = "Meeting end time cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    /** The duration of the meeting in minutes. */
    private Integer duration;

    /** The types of the meeting. */
    @NotNull(message = "Meeting types cannot be null")
    private MeetingType meetingType;

    /** The status of the meeting. */
    //Todo: Review?
//   @NotNull(message = "Meeting status cannot be null")
   private MeetingStatus meetingStatus;

    //@NotEmpty(message = "Room number cant be empty")
    //Todo: This field is nullable in the database
    private String roomNum;

    private String meetingLink;

    /** The address ID where the meeting is being held. */
    @NotNull(message = "Address ID cannot be null")
    private Long addressId;

    private String universityName;
    private String campusName;

    //@NotEmpty(message = "Participants cannot be empty")
    //Todo: This could be left? Why should we expose details of other participants?
//    private List<Long> participantIds;
}
