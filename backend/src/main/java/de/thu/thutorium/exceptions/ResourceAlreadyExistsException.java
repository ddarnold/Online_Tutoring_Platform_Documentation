package de.thu.thutorium.exceptions;

/** Exception thrown when a requested resource already exists. */
public class ResourceAlreadyExistsException extends RuntimeException {
  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
