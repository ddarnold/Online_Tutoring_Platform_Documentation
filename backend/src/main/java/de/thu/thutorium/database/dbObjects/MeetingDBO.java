package de.thu.thutorium.database.dbObjects;

import de.thu.thutorium.database.dbObjects.enums.MeetingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a meeting entity in the system, which includes details about a particular meeting such
 * as its date, type, status, related course, and more.
 *
 * <p>This entity is mapped to the "meeting" table in the database. Each meeting has a unique ID,
 * and is associated with a course and an address. The meeting also includes fields for date, type,
 * status, room number, duration of the meeting, and a link to the meeting.
 */
@Builder(toBuilder = true)
@Entity
@Table(
    name = "meeting",
    uniqueConstraints =
        @UniqueConstraint(
            name = "unique_meeting_constraint",
            columnNames = {"meeting_date", "meeting_start_time", "room_number", "address_id"}))
@Getter
@Setter
@AllArgsConstructor
public class MeetingDBO {
  /** The unique identifier for the meeting. This ID is generated automatically. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "meeting_id")
  private Long meetingId;

  /**
   * Defines who created the meeting. The association is managed as a many-to-one relationship with
   * a user with 'TUTOR' role {@link UserDBO}. The counterpart is defined as a List<MeetingDBO>
   * meetingsScheduled in {@link UserDBO}
   */
  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false)
  private UserDBO tutor;

  /**
   * The course to which this meeting is related. The association is managed as a many-to-one
   * relationship with {@link CourseDBO}. The counterpart is defined as a List<MeetingDBO> called
   * 'meetings' in {@link CourseDBO}
   */
  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private CourseDBO course;

  //Todo: DO we need date, since it is already contained in the time?
//  /** The date on which the meeting is scheduled to be held. This field cannot be null. */
//  @Column(name = "meeting_date", nullable = false)
//  @FutureOrPresent(message = "The meeting date must be in the present or future.")
  @Transient
  private LocalDate meetingDate;

  /** The time on which the meeting is scheduled to be held. This field cannot be null. */
  @Column(name = "meeting_start_time", nullable = false)
  @FutureOrPresent(message = "The meeting start time must be in the present or future.")
  private LocalDateTime startTime;

  @Column(name = "meeting_end_time", nullable = false)
  private LocalDateTime endTime;

  /** The duration of the meeting in minutes. */
  @Transient
  private Long duration;

  /**
   * Initializes transient fields after the entity is loaded from the database.
   */
  @PostLoad
  @PostPersist
  private void onLoad() {
    this.duration = Duration.between(startTime, endTime).toMinutes();
    this.meetingDate = startTime.toLocalDate();
  }

  /**
   * The type of the meeting as enumerated by the {@link MeetingType}. Represents whether the
   * meeting is online or offline.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "meeting_type", nullable = false)
  private MeetingType meetingType;

  /**
   * The room number where the meeting is taking place, if applicable. This field may be null and
   * can have a length of up to 50 characters.
   */
  @Column(name = "room_number", length = 50)
  private String roomNum;

  /**
   * The link to access the meeting, often in the form of a URL. This is stored as text in the
   * database.
   */
  @Column(name = "meeting_link", columnDefinition = "TEXT")
  private String meetingLink;

  /**
   * The address where the meeting is being held. This is managed as a bidirectional many-to-one
   * relationship. The counterpart is denoted by a List<MeetingDBO> meetings in {@link AddressDBO}.
   */
  @ManyToOne
  @JoinColumn(name = "address_id")
  private AddressDBO address;

  /**
   * Participants of this meeting.
   *
   * <p>Defines a many-to-many relationship with {@link UserDBO}, the participants in the meetings.
   */
  @ManyToMany(mappedBy = "meetings")
  @Builder.Default
  private List<UserDBO> participants = new ArrayList<>();

  @Column(name = "time_range", columnDefinition = "tsrange", insertable = false, updatable = false)
  private String timeRange;

  public MeetingDBO() {
    this.participants = new ArrayList<>();
  }
}
