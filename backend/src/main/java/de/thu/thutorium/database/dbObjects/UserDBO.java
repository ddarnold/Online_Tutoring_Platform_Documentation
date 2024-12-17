package de.thu.thutorium.database.dbObjects;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a user account entity within the system. This entity is mapped to the "user_account"
 * table in the database.
 *
 * <p>Includes basic user information such as first name, last name, role, verification status, and
 * the account creation timestamp.
 *
 * <p>Lombok annotations are used to automatically generate boilerplate code like getters, setters,
 * and constructors.
 */
@Builder// If Builder is intended to be used
@Entity
@Table(name = "user_account")
@Getter
@Setter
@AllArgsConstructor
public class UserDBO implements UserDetails {
  /**
   * The unique identifier for the user. This value is automatically generated by the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  @Setter(AccessLevel.NONE)
  private Long userId;

  /** The first name of the user. This field is mandatory and cannot be null. */
  @Column(name = "first_name", nullable = false)
  private String firstName;

  /** The last name of the user. This field is mandatory and cannot be null. */
  @Column(name = "last_name", nullable = false)
  private String lastName;

  /** The user's email, used for login. This field must be unique. */
  @Column(name = "email_address", nullable = false, unique = true)
  private String email;

  /** The hashed password for authentication. This field is mandatory. */
  @Column(name = "hashed_password", nullable = false)
  private String password;

  /**
   * The roles of the user within the system, such as STUDENT, TUTOR etc. Multiple roles are foreseen:
    * a tutor could also be a student,
    * an admin could be a verifier.
   * The user roles are resolved as having many-to-many relations with the user.
   * The counterpart is a Set<UserDBO> called 'users' in {@link RoleDBO}
   */
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  @Builder.Default
  private Set<RoleDBO> roles = new HashSet<>();

  /**
   * Defines a many-to-one relationship between a user and their affiliation with respect to the university.
   * <p>
   * This relationship is mapped by the {@code affiliation_id} foreign key column in the {@link UserDBO} entity.
   * The {@code affiliation} field represents the affiliation to which the user belongs.
   * The counterpart is denoted by a List<UserDBO> called 'affiliatedUsers' in the {@link AffiliationDBO}.
   */
  @ManyToOne
  @JoinColumn(name = "affiliation_id")
  private AffiliationDBO affiliation;

  /**
   * A textual description of the user.
   */
  @Column(name = "user_description", columnDefinition = "TEXT")
  private String description;

  /**
   * The timestamp when the user account was created. This field is initialised with current time.
   */
  @Column(name = "created_at")
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();


  /** Indicates whether the user's email is verified. Defaults to {@code false} if not specified. */
  @Column(name = "is_verified")
  @Builder.Default
  private Boolean isVerified = false;

  /**
   * The timestamp when the user account was verified.
   */
  @Column(name = "verified_on")
  private LocalDateTime verified_on;


  /**
   * Verifiers for this user.
   * <p>
   * Defines a many-to-many relationship with {@link UserDBO} using the join table "users_verifiers" for defining the
   * verifiers who can verify other users. It is s uni-directional relationship(!!), since the users do not know who has
   * verified them.
   */
  @ManyToMany
  @JoinTable(name = "users_verifiers",
          joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "verifier_id", referencedColumnName = "user_id")
  )
  @Builder.Default
  private Set<UserDBO> verifiers = new HashSet<>();

  /**
   * Describes if the user is enabled or not. Default value: True.
   */
  @Column(name = "enabled")
  @Builder.Default
  private Boolean enabled = true;

  /**
   * Represents the list of courses associated with this user if they are a tutor.
   * <p>This relationship is mapped by the {@code tutor} field in the {@link CourseDBO} entity. The
   * cascade type {@code CascadeType.ALL} ensures that all operations (such as persist and remove)
   * are propagated to the associated courses.
   * <p>If this user is deleted, all their associated courses will also be deleted due to the
   * cascading operations defined in this relationship.
   * The counterpart is denoted by a Set<UserDBO> called 'participants' in the {@link CourseDBO}.
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "course_students",
          joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id")
  )
  @Builder.Default
  private Set<CourseDBO> studentCourses = new HashSet<>();

  /**
   * Ratings given by a student to tutors.
   *<p> Defines a one-to-many relationship with {@link RatingTutorDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the student.
   */
  @OneToMany(mappedBy = "student", orphanRemoval = true)
  private List<RatingTutorDBO> givenTutorRatings;

  /**
   * Ratings received by a tutor from students.
   * <p> Defines a one-to-many relationship with {@link RatingTutorDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the tutor.
   */
  @OneToMany(mappedBy = "tutor", orphanRemoval = true)
  @Builder.Default
  private List<RatingTutorDBO> receivedTutorRatings = new ArrayList<>();

  /**
   * Ratings given by this student to courses.
   *<p> Defines a one-to-many relationship with {@link RatingCourseDBO}.
   * The cascade type {@code ALL} ensures that all operations are propagated to the associated ratings.
   * The {@code orphanRemoval} attribute ensures that ratings are removed if they are no longer associated with the student.
   */
  @OneToMany(mappedBy = "student", orphanRemoval = true)
  @Builder.Default
  private List<RatingCourseDBO> givenCourseRatings = new ArrayList<>();

  /**
   * Meetings scheduled for the users.
   * <p>
   * Defines a many-to-many relationship with {@link UserDBO} using the join table "users_meetings" for defining the
   * meetings scheduled for the users. It is s bidirectional relationship(!!), the counterpart denoted as Set<UserDBO>
   * called 'participants' in {@link MeetingDBO}.
   */
  @ManyToMany
  @JoinTable(name = "students_meetings",
          joinColumns = @JoinColumn(name = "student_id"),
          inverseJoinColumns = @JoinColumn(name = "meeting_id")
  )
  @Builder.Default
  private List<MeetingDBO> meetings = new ArrayList<>();

  /**
   * The scores received by a student for the courses they attend.
   *<p> Defines a one-to-many relationship with {@link UserDBO}.
   * The {@code orphanRemoval} attribute ensures that scores are removed if they are no longer associated with the student.
   */
  @OneToMany(mappedBy = "student", orphanRemoval = true)
  @Builder.Default
  private List<ProgressDBO> receivedScores = new ArrayList<>();


  /**
   * The list of course categories created by a user with admin role.
   * <p> Defines a one-to-many relationship with {@link CourseCategoryDBO}.
   */
  @OneToMany(mappedBy = "created_by")
  @Builder.Default
  private List<CourseCategoryDBO> courseCategories = new ArrayList<>();

  /**
   * The list of meetings created by a user with tutor role.
   * <p> Defines a one-to-many relationship with {@link MeetingDBO}.
   */
  @OneToMany(mappedBy = "tutor")
  @Builder.Default
  private List<MeetingDBO> meetingsScheduled = new ArrayList<>();

  /**
   * The list of courses created by a user with tutor role.
   * <p> Defines a one-to-many relationship with {@link CourseDBO}.
   */
  @OneToMany(mappedBy = "tutor", orphanRemoval = true)
  @Builder.Default
  private List<CourseDBO> tutorCourses = new ArrayList<>();

  /**
   * Constructs a UserDBO object with default values.
   */
  public UserDBO() {
    this.roles = new HashSet<>();
    this.verifiers = new HashSet<>();
    this.studentCourses = new HashSet<>();
    this.tutorCourses = new ArrayList<>();
    this.givenTutorRatings = new ArrayList<>();
    this.receivedTutorRatings = new ArrayList<>();
    this.givenCourseRatings = new ArrayList<>();
    this.meetings = new ArrayList<>();
    this.receivedScores = new ArrayList<>();
    this.courseCategories = new ArrayList<>();
    this.meetingsScheduled = new ArrayList<>();
  }

  /**
   * Returns the authorities granted to the user.
   * @return a collection of granted authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name())) // Ensure prefix 'ROLE_'
            .collect(Collectors.toSet());
  }

  /**
   * Returns the username of the user; in this case, the email
   * @return the email
   */
  @Override
  public String getUsername() {
    return email;
  }

  /**
   * Returns the password of the user.
   * @return the password
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Indicates whether the user's account has expired.
   * @return true if the account is non-expired, false otherwise
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is locked or unlocked.
   * @return true if the account is non-locked, false otherwise
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indicates whether the user's credentials (password) has expired.
   * @return true if the credentials are non-expired, false otherwise
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is enabled or disabled.
   * @return true if the user is enabled, false otherwise
   */
  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public String toString() {
    return "UserDBO {"
            + "id=" + userId
            + ", email='" + email + '\''
            + ", password='[PROTECTED]'"
            + ", roles=" + roles
            + '}';
  }
}
