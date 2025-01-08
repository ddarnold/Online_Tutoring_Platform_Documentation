package de.thu.thutorium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) //Returns 409 error code for conflict of entries
public class MeetingConflictException extends RuntimeException {
  public MeetingConflictException(String message) {
    super(message);
  }
}
