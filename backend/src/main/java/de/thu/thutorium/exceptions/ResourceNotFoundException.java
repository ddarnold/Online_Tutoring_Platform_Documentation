package de.thu.thutorium.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  //    private static final String MESSAGE = "Resource not found";
  /**
   * Constructs a new ResourceNotFoundException with a default message. It calls the superclass
   * constructor super(MESSAGE), passing the default message "Resource not found" to the
   * RuntimeException class.
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
