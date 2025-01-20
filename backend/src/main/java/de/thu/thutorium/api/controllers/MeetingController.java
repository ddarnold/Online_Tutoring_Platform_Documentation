package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.api.transferObjects.common.UserTO;
import de.thu.thutorium.services.interfaces.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/meetings")
public class MeetingController {

  private final MeetingService meetingService;

  public MeetingController(MeetingService meetingService) {
    this.meetingService = meetingService;
  }

  // Endpoint to book a meeting
  @PostMapping("/book/{meetingId}")
  @ResponseStatus(HttpStatus.CREATED)
  public void bookMeeting(@PathVariable Long meetingId) {
    meetingService.bookMeeting(meetingId);
  }

  // Endpoint to cancel a meeting
  @DeleteMapping("/cancel/{meetingId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelMeeting(@PathVariable Long meetingId) {
    meetingService.cancelMeeting(meetingId);
  }

  // Endpoint to retrieve a meeting by its ID
  @GetMapping("/{meetingId}")
  public MeetingTO retrieveMeetingById(@PathVariable Long meetingId) {
    return meetingService.retrieveMeetingById(meetingId);
  }

  // Endpoint to retrieve all meetings by a specific course ID
  @GetMapping("/course/{courseId}")
  public List<MeetingTO> retrieveMeetingsByCourse(@PathVariable Long courseId) {
    return meetingService.retrieveMeetingsByCourse(courseId);
  }

  // Endpoint to retrieve all participants of a meeting
  @GetMapping("/{meetingId}/participants")
  public List<UserTO> retrieveAllParticipants(@PathVariable Long meetingId) {
    return meetingService.retrieveAllParticipants(meetingId);
  }
}
