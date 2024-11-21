package de.thu.thutorium.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RatingTest {

  private Rating rating;
  private User student;
  private User tutor;
  private Course course;

  @BeforeEach
  void setUp() {
    student = new User();
    student.setUserId(1L);
    student.setFirstName("Alice");
    student.setLastName("Smith");
    student.setRole(UserRole.STUDENT);

    tutor = new User();
    tutor.setUserId(2L);
    tutor.setFirstName("Bob");
    tutor.setLastName("Jones");
    tutor.setRole(UserRole.TUTOR);

    course = new Course();
    course.setCourseId(1L);
    course.setCourseName("Java Basics");

    rating = new Rating();
    rating.setRatingId(1L);
    rating.setRatedUser(tutor);
    rating.setStudent(student);
    rating.setCourse(course);
    rating.setPoints(5);
    rating.setReview("Great tutor and course!");
    rating.setRatingType(RatingType.TUTOR);
  }

  @Test
  void testRatingFields() {
    assertEquals(1L, rating.getRatingId());
    assertEquals(tutor, rating.getRatedUser());
    assertEquals(student, rating.getStudent());
    assertEquals(course, rating.getCourse());
    assertEquals(5, rating.getPoints());
    assertEquals("Great tutor and course!", rating.getReview());
    assertEquals(RatingType.TUTOR, rating.getRatingType());
  }

  @Test
  void testSettersAndGetters() {
    rating.setPoints(4);
    assertEquals(4, rating.getPoints());

    rating.setReview("Good, but can improve.");
    assertEquals("Good, but can improve.", rating.getReview());

    rating.setRatingType(RatingType.COURSE);
    assertEquals(RatingType.COURSE, rating.getRatingType());
  }

  @Test
  void testDefaultConstructor() {
    Rating emptyRating = new Rating();
    assertNull(emptyRating.getRatingId());
    assertNull(emptyRating.getRatedUser());
    assertNull(emptyRating.getStudent());
    assertNull(emptyRating.getCourse());
    assertNull(emptyRating.getPoints());
    assertNull(emptyRating.getReview());
    assertNull(emptyRating.getRatingType());
  }

  @Test
  void testAllArgsConstructor() {
    Rating fullRating =
        new Rating(2L, tutor, student, course, 4, "Well structured course.", RatingType.COURSE);

    assertEquals(2L, fullRating.getRatingId());
    assertEquals(tutor, fullRating.getRatedUser());
    assertEquals(student, fullRating.getStudent());
    assertEquals(course, fullRating.getCourse());
    assertEquals(4, fullRating.getPoints());
    assertEquals("Well structured course.", fullRating.getReview());
    assertEquals(RatingType.COURSE, fullRating.getRatingType());
  }

  @Test
  void testRelationshipsInitialization() {
    assertNotNull(rating.getStudent());
    assertNotNull(rating.getRatedUser());
    assertNotNull(rating.getCourse());
  }

  @Test
  void testEquality() {
    Rating anotherRating =
        new Rating(1L, tutor, student, course, 5, "Great tutor and course!", RatingType.TUTOR);

    assertEquals(rating, anotherRating);
  }
}
