package de.thu.thutorium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Thutorium Spring Boot application.
 *
 * <p>This class is annotated with {@link SpringBootApplication}, which is a convenience annotation
 * that includes {@code @Configuration}, {@code @EnableAutoConfiguration}, and
 * {@code @ComponentScan}.
 *
 * <p>The {@code main} method uses {@link SpringApplication#run(Class, String...)} to launch the
 * application.
 */
@SpringBootApplication
public class ThutoriumApplication {

  /**
   * Main method which serves as the entry point of the application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(ThutoriumApplication.class, args);
  }
}
