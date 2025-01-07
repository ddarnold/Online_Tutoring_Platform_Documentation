package de.thu.thutorium.api.transferObjects.common;

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
  @NotNull(message = "Meeting start time cannot be null")
  private LocalDateTime startTime;

  /** The time of the meeting. */
  @NotNull(message = "Meeting end time cannot be null")
  private LocalDateTime endTime;

  /** The duration of the meeting in minutes. */
  @NotNull(message = "Duration cannot be null")
  private Integer duration;

  /** The types of the meeting. */
  @NotNull(message = "Meeting types cannot be null")
  private MeetingType meetingType;

  @NotEmpty(message = "Participants cannot be empty")
  private List<Long> participantIds;

  /** The address ID where the meeting is being held. */
  private Long addressId;

  private String roomNum;

  private String campusName;

  private String universityName;

  private String meetingLink;
}
