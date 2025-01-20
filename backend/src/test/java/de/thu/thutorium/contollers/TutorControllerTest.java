package de.thu.thutorium.contollers;

import de.thu.thutorium.api.controllers.TutorController;
import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.api.transferObjects.common.ProgressTO;
import de.thu.thutorium.services.interfaces.CourseService;
import de.thu.thutorium.services.interfaces.MeetingService;
import de.thu.thutorium.services.interfaces.ProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorControllerTest {

    @Mock
    private MeetingService meetingService;

    @Mock
    private CourseService courseService;

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private TutorController tutorController;

    private MeetingTO meetingTO;
    private CourseTO courseTO;
    private ProgressTO progressTO;

    @BeforeEach
    void setUp() {
        meetingTO = MeetingTO.builder()
                .tutorId(1L)
                .courseId(1L)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .duration_in_minutes(60L)
                .meetingType(null)
                .addressId(100L)
                .roomNum("101")
                .campusName("Main Campus")
                .universityName("Example University")
                .meetingLink("http://meeting-link.com")
                .build();

        courseTO = new CourseTO();
        courseTO.setCourseId(1L);
        courseTO.setCourseName("Sample Course");
        courseTO.setTutorId(1L);
        courseTO.setTutorName("John Doe");
        courseTO.setDescriptionShort("Short description of the course");
        courseTO.setDescriptionLong("Detailed description of the course");
        courseTO.setStartDate(LocalDate.now());
        courseTO.setEndDate(LocalDate.now().plusMonths(1));
        courseTO.setAverageRating(4.5);
        courseTO.setCourseCategories(List.of());

        progressTO = new ProgressTO();
        progressTO.setStudentId(1L);
        progressTO.setCourseId(1L);
        progressTO.setPoints(80.0);
    }

    @Test
    void createMeeting_Success() {
        when(meetingService.createMeeting(meetingTO)).thenReturn(meetingTO);

        ResponseEntity<?> response = tutorController.createMeeting(meetingTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(meetingTO, response.getBody());
        verify(meetingService, times(1)).createMeeting(meetingTO);
    }

    @Test
    void deleteMeeting_Success() {
        doNothing().when(meetingService).deleteMeeting(1L);

        ResponseEntity<?> response = tutorController.deleteMeeting(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Meeting deleted successfully", response.getBody());
        verify(meetingService, times(1)).deleteMeeting(1L);
    }

    @Test
    void updateMeeting_Success() {
        when(meetingService.updateMeeting(1L, meetingTO)).thenReturn(meetingTO);

        ResponseEntity<?> response = tutorController.updateMeeting(1L, meetingTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(meetingTO, response.getBody());
        verify(meetingService, times(1)).updateMeeting(1L, meetingTO);
    }

    @Test
    void createCourse_Success() {
        when(courseService.createCourse(courseTO)).thenReturn(courseTO);

        ResponseEntity<?> response = tutorController.createCourse(courseTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(courseTO, response.getBody());
        verify(courseService, times(1)).createCourse(courseTO);
    }

    @Test
    void deleteCourse_Success() {
        doNothing().when(courseService).deleteCourse(1L);

        ResponseEntity<?> response = tutorController.deleteCourse(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Course deleted successfully", response.getBody());
        verify(courseService, times(1)).deleteCourse(1L);
    }

    @Test
    void updateCourse_Success() {
        when(courseService.updateCourse(1L, courseTO)).thenReturn(courseTO);

        ResponseEntity<?> response = tutorController.updateCourse(1L, courseTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseTO, response.getBody());
        verify(courseService, times(1)).updateCourse(1L, courseTO);
    }

    @Test
    void createProgress_Success() {
        doNothing().when(progressService).createProgress(progressTO);

        ResponseEntity<String> response = tutorController.createProgress(progressTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Progress created successfully", response.getBody());
        verify(progressService, times(1)).createProgress(progressTO);
    }

    @Test
    void deleteProgress_Success() {
        when(progressService.deleteProgress(1L, 1L)).thenReturn(true);

        ResponseEntity<String> response = tutorController.deleteProgress(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Progress deleted successfully", response.getBody());
        verify(progressService, times(1)).deleteProgress(1L, 1L);
    }

    @Test
    void deleteProgress_NotFound() {
        when(progressService.deleteProgress(1L, 1L)).thenReturn(false);

        ResponseEntity<String> response = tutorController.deleteProgress(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Progress record not found", response.getBody());
        verify(progressService, times(1)).deleteProgress(1L, 1L);
    }

    @Test
    void updateProgress_Success() {
        when(progressService.updateProgress(1L, 1L, 90.0)).thenReturn(true);

        ResponseEntity<String> response = tutorController.updateProgress(1L, 1L, 90.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Progress updated successfully", response.getBody());
        verify(progressService, times(1)).updateProgress(1L, 1L, 90.0);
    }

    @Test
    void updateProgress_NotFound() {
        when(progressService.updateProgress(1L, 1L, 90.0)).thenReturn(false);

        ResponseEntity<String> response = tutorController.updateProgress(1L, 1L, 90.0);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Progress record not found", response.getBody());
        verify(progressService, times(1)).updateProgress(1L, 1L, 90.0);
    }
}
