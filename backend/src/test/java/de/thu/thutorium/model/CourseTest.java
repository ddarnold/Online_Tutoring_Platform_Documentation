package de.thu.thutorium.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {

  private Course course;
  private User tutor;
  private Category category;
  private List<Rating> ratings;

  @BeforeEach
  void setUp() {
    // Set up a sample tutor
    tutor = new User();
    tutor.setUserId(1L);
    tutor.setFirstName("John");
    tutor.setLastName("Doe");
    tutor.setRole(UserRole.TUTOR);

    // Set up a sample category
    category = new Category();
    category.setCategoryId(1L);
    category.setCategoryName("Programming");

    // Set up sample ratings
    ratings = new ArrayList<>();
    Rating rating = new Rating();
    rating.setRatingId(1L);
    rating.setPoints(5);
    ratings.add(rating);

    // Set up a course
    course = new Course();
    course.setCourseId(1L);
    course.setCourseName("Java Basics");
    course.setDescriptionShort("Learn the basics of Java programming.");
    course.setDescriptionLong("This course will teach you the fundamentals of Java programming.");
    course.setCreatedAt(LocalDateTime.now());
    course.setStartDate(LocalDate.of(2024, 1, 15));
    course.setEndDate(LocalDate.of(2024, 3, 15));
    course.setTutor(tutor);
    course.setCategory(category);
    course.setRatings(ratings);
  }

  @Test
  void testCourseFields() {
    assertEquals(1L, course.getCourseId());
    assertEquals("Java Basics", course.getCourseName());
    assertEquals("Learn the basics of Java programming.", course.getDescriptionShort());
    assertEquals(
        "This course will teach you the fundamentals of Java programming.",
        course.getDescriptionLong());
    assertNotNull(course.getCreatedAt());
    assertEquals(LocalDate.of(2024, 1, 15), course.getStartDate());
    assertEquals(LocalDate.of(2024, 3, 15), course.getEndDate());
    assertEquals(tutor, course.getTutor());
    assertEquals(category, course.getCategory());
    assertEquals(ratings, course.getRatings());
  }

  @Test
  void testSettersAndGetters() {
    course.setCourseName("Advanced Java");
    assertEquals("Advanced Java", course.getCourseName());

    course.setDescriptionShort("Master advanced concepts in Java.");
    assertEquals("Master advanced concepts in Java.", course.getDescriptionShort());

    course.setDescriptionLong(
        "This course covers advanced Java topics, including multithreading and networking.");
    assertEquals(
        "This course covers advanced Java topics, including multithreading and networking.",
        course.getDescriptionLong());

    course.setStartDate(LocalDate.of(2024, 5, 1));
    assertEquals(LocalDate.of(2024, 5, 1), course.getStartDate());

    course.setEndDate(LocalDate.of(2024, 7, 1));
    assertEquals(LocalDate.of(2024, 7, 1), course.getEndDate());
  }

  @Test
  void testDefaultConstructor() {
    Course emptyCourse = new Course();
    assertNull(emptyCourse.getCourseId());
    assertNull(emptyCourse.getCourseName());
    assertNull(emptyCourse.getDescriptionShort());
    assertNull(emptyCourse.getDescriptionLong());
    assertNull(emptyCourse.getCreatedAt());
    assertNull(emptyCourse.getStartDate());
    assertNull(emptyCourse.getEndDate());
    assertNull(emptyCourse.getTutor());
    assertNull(emptyCourse.getCategory());
    assertNull(emptyCourse.getRatings());
  }

  @Test
  void testAllArgsConstructor() {
    Course fullCourse =
        new Course(
            2L,
            tutor,
            "Spring Boot Essentials",
            "Learn Spring Boot fundamentals.",
            "This course covers building REST APIs using Spring Boot.",
            LocalDateTime.of(2024, 1, 1, 10, 0),
            LocalDate.of(2024, 2, 1),
            LocalDate.of(2024, 4, 1),
            category,
            ratings);

    assertEquals(2L, fullCourse.getCourseId());
    assertEquals(tutor, fullCourse.getTutor());
    assertEquals("Spring Boot Essentials", fullCourse.getCourseName());
    assertEquals("Learn Spring Boot fundamentals.", fullCourse.getDescriptionShort());
    assertEquals(
        "This course covers building REST APIs using Spring Boot.",
        fullCourse.getDescriptionLong());
    assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), fullCourse.getCreatedAt());
    assertEquals(LocalDate.of(2024, 2, 1), fullCourse.getStartDate());
    assertEquals(LocalDate.of(2024, 4, 1), fullCourse.getEndDate());
    assertEquals(category, fullCourse.getCategory());
    assertEquals(ratings, fullCourse.getRatings());
  }

  @Test
  void testRatingsAssociation() {
    Rating newRating = new Rating();
    newRating.setRatingId(2L);
    newRating.setPoints(4);

    ratings.add(newRating);
    course.setRatings(ratings);

    assertEquals(2, course.getRatings().size());
    assertTrue(course.getRatings().contains(newRating));
  }

  @Test
  void testEquality() {
    Course anotherCourse =
        new Course(
            1L,
            tutor,
            "Java Basics",
            "Learn the basics of Java programming.",
            "This course will teach you the fundamentals of Java programming.",
            course.getCreatedAt(),
            LocalDate.of(2024, 1, 15),
            LocalDate.of(2024, 3, 15),
            category,
            ratings);

    assertEquals(course, anotherCourse);
  }
}
