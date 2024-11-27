package de.thu.thutorium.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class UserTest {

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUserId(1L);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setRole(UserRole.STUDENT);
    user.setIsVerified(true);
    user.setCreatedAt(LocalDateTime.now());
    user.setEmail("john.doe@example.com");
    user.setHashedPassword("hashed_password_example");
    user.setTutor_description(null);
  }

  @Test
  void testUserFields() {
    assertEquals(1L, user.getUserId());
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals(UserRole.STUDENT, user.getRole());
    assertTrue(user.getIsVerified());
    assertNotNull(user.getCreatedAt());
    assertEquals("john.doe@example.com", user.getEmail());
    assertEquals("hashed_password_example", user.getHashedPassword());
    assertNull(user.getTutor_description());
  }

  @Test
  void testDefaultVerificationStatus() {
    User newUser = new User();
    assertFalse(newUser.getIsVerified()); // Default value should be false
  }

  @Test
  void testRelationshipsInitialization() {
    assertNull(user.getCourses());
    assertNull(user.getRatings());
  }

  @Test
  void testAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();
    List<Course> courses = List.of(new Course());
    List<Rating> ratings = List.of(new Rating());

    User fullUser =
            new User(
                    2L,
                    "Jane",
                    "Smith",
                    UserRole.TUTOR,
                    false,
                    now,
                    courses,
                    ratings,
                    "jane.smith@example.com",
                    "hashed_password_example",
                    "Expert tutor in science");

    assertEquals(2L, fullUser.getUserId());
    assertEquals("Jane", fullUser.getFirstName());
    assertEquals("Smith", fullUser.getLastName());
    assertEquals(UserRole.TUTOR, fullUser.getRole());
    assertFalse(fullUser.getIsVerified());
    assertEquals(now, fullUser.getCreatedAt());
    assertEquals(courses, fullUser.getCourses());
    assertEquals(ratings, fullUser.getRatings());
    assertEquals("jane.smith@example.com", fullUser.getEmail());
    assertEquals("hashed_password_example", fullUser.getHashedPassword());
    assertEquals("Expert tutor in science", fullUser.getTutor_description());
  }

  @Test
  void testNoArgsConstructor() {
    User emptyUser = new User();
    assertNull(emptyUser.getUserId());
    assertNull(emptyUser.getFirstName());
    assertNull(emptyUser.getLastName());
    assertNull(emptyUser.getRole());
    assertNull(emptyUser.getCreatedAt());
    assertNull(emptyUser.getCourses());
    assertNull(emptyUser.getRatings());
    assertNull(emptyUser.getEmail());
    assertNull(emptyUser.getHashedPassword());
    assertNull(emptyUser.getTutor_description());
  }

  @Test
  void testSettersAndGetters() {
    user.setFirstName("Michael");
    assertEquals("Michael", user.getFirstName());

    user.setLastName("Johnson");
    assertEquals("Johnson", user.getLastName());

    user.setRole(UserRole.TUTOR);
    assertEquals(UserRole.TUTOR, user.getRole());

    user.setEmail("michael.johnson@example.com");
    assertEquals("michael.johnson@example.com", user.getEmail());

    user.setHashedPassword("new_hashed_password");
    assertEquals("new_hashed_password", user.getHashedPassword());

    user.setTutor_description("Specialized in programming");
    assertEquals("Specialized in programming", user.getTutor_description());
  }
}
