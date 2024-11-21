package de.thu.thutorium.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
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
  }

  @Test
  void testUserFields() {
    assertEquals(1L, user.getUserId());
    assertEquals("John", user.getFirstName());
    assertEquals("Doe", user.getLastName());
    assertEquals(UserRole.STUDENT, user.getRole());
    assertTrue(user.getIsVerified());
    assertNotNull(user.getCreatedAt());
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
    assertNull(user.getCredentials());
  }

  @Test
  void testAllArgsConstructor() {
    LocalDateTime now = LocalDateTime.now();
    List<Course> courses = List.of(new Course());
    List<Rating> ratings = List.of(new Rating());

    User fullUser =
        new User(2L, "Jane", "Smith", UserRole.TUTOR, false, now, courses, ratings, null);

    assertEquals(2L, fullUser.getUserId());
    assertEquals("Jane", fullUser.getFirstName());
    assertEquals("Smith", fullUser.getLastName());
    assertEquals(UserRole.TUTOR, fullUser.getRole());
    assertFalse(fullUser.getIsVerified());
    assertEquals(now, fullUser.getCreatedAt());
    assertEquals(courses, fullUser.getCourses());
    assertEquals(ratings, fullUser.getRatings());
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
    assertNull(emptyUser.getCredentials());
  }

  @Test
  void testSettersAndGetters() {
    user.setFirstName("Michael");
    assertEquals("Michael", user.getFirstName());

    user.setLastName("Johnson");
    assertEquals("Johnson", user.getLastName());

    user.setRole(UserRole.TUTOR);
    assertEquals(UserRole.TUTOR, user.getRole());
  }

  @Test
  void testUserEquality() {
    User anotherUser =
        new User(1L, "John", "Doe", UserRole.STUDENT, true, user.getCreatedAt(), null, null, null);
    assertEquals(user, anotherUser);
  }
}
