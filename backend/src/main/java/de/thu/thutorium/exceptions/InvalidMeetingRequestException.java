package de.thu.thutorium.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP 400 for bad input scenarios
public class InvalidMeetingRequestException extends RuntimeException {
    public InvalidMeetingRequestException(String message) {
        super(message);
    }
}
