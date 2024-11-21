package de.thu.thutorium.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CredentialsTest {

  private Credentials credentials;
  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setUserId(1L);
    user.setFirstName("Alice");
    user.setLastName("Smith");
    user.setRole(UserRole.STUDENT);

    credentials = new Credentials();
    credentials.setCredentialsId(1L);
    credentials.setEmail("alice.smith@example.com");
    credentials.setHashedPassword("hashed_password_123");
    credentials.setUser(user);
  }

  @Test
  void testCredentialsFields() {
    assertEquals(1L, credentials.getCredentialsId());
    assertEquals("alice.smith@example.com", credentials.getEmail());
    assertEquals("hashed_password_123", credentials.getHashedPassword());
    assertEquals(user, credentials.getUser());
  }

  @Test
  void testSettersAndGetters() {
    credentials.setEmail("new.email@example.com");
    assertEquals("new.email@example.com", credentials.getEmail());

    credentials.setHashedPassword("new_hashed_password");
    assertEquals("new_hashed_password", credentials.getHashedPassword());

    User newUser = new User();
    newUser.setUserId(2L);
    newUser.setFirstName("Bob");
    newUser.setLastName("Jones");
    newUser.setRole(UserRole.TUTOR);

    credentials.setUser(newUser);
    assertEquals(newUser, credentials.getUser());
  }

  @Test
  void testDefaultConstructor() {
    Credentials emptyCredentials = new Credentials();
    assertNull(emptyCredentials.getCredentialsId());
    assertNull(emptyCredentials.getEmail());
    assertNull(emptyCredentials.getHashedPassword());
    assertNull(emptyCredentials.getUser());
  }

  @Test
  void testAllArgsConstructor() {
    Credentials fullCredentials =
        new Credentials(2L, "bob.jones@example.com", "hashed_password_456", user);

    assertEquals(2L, fullCredentials.getCredentialsId());
    assertEquals("bob.jones@example.com", fullCredentials.getEmail());
    assertEquals("hashed_password_456", fullCredentials.getHashedPassword());
    assertEquals(user, fullCredentials.getUser());
  }

  @Test
  void testEquality() {
    Credentials anotherCredentials =
        new Credentials(1L, "alice.smith@example.com", "hashed_password_123", user);

    assertEquals(credentials, anotherCredentials);
  }
}
