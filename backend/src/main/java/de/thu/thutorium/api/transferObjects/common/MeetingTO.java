package de.thu.thutorium.api.transferObjects.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.thu.thutorium.database.dbObjects.enums.MeetingType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class MeetingTO {
  /** The tutor who created the meeting. */
  private Long meetingId;

  private Long tutorId;

  private String tutorName;

  /** The course to which this meeting is related. */
  @NotNull(message = "Course ID cannot be null")
  private Long courseId;

  private String courseName;

  private LocalDate meetingDate;

  /** The time of the meeting. */
  @NotNull(message = "Meeting time cannot be null")
  @FutureOrPresent(message = "The meeting start time must be in the present or future.")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startTime;

  /** The time of the meeting. */
  @NotNull(message = "Meeting end time cannot be null")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endTime;

  /** The duration of the meeting in minutes. */
  private Long duration_in_minutes;

  /** The types of the meeting. */
  @NotNull(message = "Meeting types cannot be null")
  private MeetingType meetingType;

  /** The address ID where the meeting is being held. */
  private Long addressId;

  private String roomNum;

  private String campusName;

  private String universityName;

  private String meetingLink;
}
