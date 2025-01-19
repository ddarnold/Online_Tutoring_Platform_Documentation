package de.thu.thutorium;

import de.thu.thutorium.api.TOMappers.MeetingTOMapper;
import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.database.DBOMappers.MeetingDBMapper;
import de.thu.thutorium.database.dbObjects.*;
import de.thu.thutorium.database.repositories.*;
import de.thu.thutorium.services.implementations.MeetingServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

    private static final Long VALID_TUTOR_ID = 1L;
    private static final Long VALID_COURSE_ID = 1L;
    private static final Long VALID_ADDRESS_ID = 1L;
    private static final Long VALID_MEETING_ID = 1L;
    private static final Long INVALID_ID = 99L;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MeetingDBMapper meetingDBMapper;

    @Mock
    private MeetingTOMapper meetingTOMapper;

    @InjectMocks
    private MeetingServiceImpl meetingService;

    @Captor
    private ArgumentCaptor<MeetingDBO> meetingCaptor;

    private UserDBO tutor;
    private CourseDBO course;
    private AddressDBO address;
    private MeetingTO meetingTO;
    private MeetingDBO meetingDBO;

    @BeforeEach
    void setUp() {
        tutor = UserDBO.builder()
                .userId(VALID_TUTOR_ID)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword")
                .build();

        course = CourseDBO.builder()
                .courseId(VALID_COURSE_ID)
                .courseName("Java 101")
                .build();

        address = AddressDBO.builder()
                .addressId(VALID_ADDRESS_ID)
                .streetName("123 Main St")
                .city("Ulm")
                .build();

        meetingTO = new MeetingTO();
        meetingTO.setTutorId(VALID_TUTOR_ID);
        meetingTO.setCourseId(VALID_COURSE_ID);
        meetingTO.setAddressId(VALID_ADDRESS_ID);

        meetingDBO = new MeetingDBO();
        meetingDBO.setTutor(tutor);
        meetingDBO.setCourse(course);
        meetingDBO.setAddress(address);
    }

    @Test
    void createMeeting_success() {
        // Arrange
        when(userRepository.findById(VALID_TUTOR_ID)).thenReturn(Optional.of(tutor));
        when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(course));
        when(addressRepository.findById(VALID_ADDRESS_ID)).thenReturn(Optional.of(address));
        when(meetingDBMapper.toEntity(meetingTO)).thenReturn(meetingDBO);

        // Act
        meetingService.createMeeting(meetingTO);

        // Assert
        verify(meetingRepository, times(1)).save(meetingCaptor.capture());
        MeetingDBO capturedMeeting = meetingCaptor.getValue();
        assertEquals(tutor, capturedMeeting.getTutor());
        assertEquals(course, capturedMeeting.getCourse());
        assertEquals(address, capturedMeeting.getAddress());
    }

    @Test
    void createMeeting_tutorNotFound_throwsException() {
        // Arrange
        when(userRepository.findById(VALID_TUTOR_ID)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            meetingService.createMeeting(meetingTO);
        });
        assertEquals("Tutor not found with ID: 1", exception.getMessage());
        verifyNoInteractions(courseRepository, addressRepository, meetingRepository);
    }

    @Test
    void deleteMeeting_meetingExists_success() {
        // Arrange
        when(meetingRepository.existsById(VALID_MEETING_ID)).thenReturn(true);

        // Act
        meetingService.deleteMeeting(VALID_MEETING_ID);

        // Assert
        verify(meetingRepository, times(1)).deleteById(VALID_MEETING_ID);
    }

    @Test
    void deleteMeeting_meetingDoesNotExist_throwsException() {
        // Arrange
        when(meetingRepository.existsById(INVALID_ID)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            meetingService.deleteMeeting(INVALID_ID);
        });
        assertEquals("Meeting not found with ID: 99", exception.getMessage());
        verify(meetingRepository, never()).deleteById(anyLong());
    }

    @Test
    void updateMeeting_success() {
        // Arrange
        MeetingTO updatedMeetingTO = new MeetingTO();
        updatedMeetingTO.setTutorId(VALID_TUTOR_ID);
        updatedMeetingTO.setCourseId(VALID_COURSE_ID);
        updatedMeetingTO.setAddressId(VALID_ADDRESS_ID);

        when(meetingRepository.findById(VALID_MEETING_ID)).thenReturn(Optional.of(meetingDBO));
        when(userRepository.findById(VALID_TUTOR_ID)).thenReturn(Optional.of(tutor));
        when(courseRepository.findById(VALID_COURSE_ID)).thenReturn(Optional.of(course));
        when(addressRepository.findById(VALID_ADDRESS_ID)).thenReturn(Optional.of(address));

        // Act
        meetingService.updateMeeting(VALID_MEETING_ID, updatedMeetingTO);

        // Assert
        verify(meetingRepository, times(1)).save(meetingCaptor.capture());
        MeetingDBO capturedMeeting = meetingCaptor.getValue();
        assertEquals(tutor, capturedMeeting.getTutor());
        assertEquals(course, capturedMeeting.getCourse());
        assertEquals(address, capturedMeeting.getAddress());
    }

    @Test
    void getMeetingsForUser_validUser_success() {
        // Arrange
        List<MeetingDBO> meetings = List.of(meetingDBO);
        when(meetingRepository.findScheduledMeetingsByTutorId(VALID_TUTOR_ID)).thenReturn(meetings);
        when(meetingTOMapper.toDTOList(meetings)).thenReturn(List.of(meetingTO));

        // Act
        List<MeetingTO> result = meetingService.getMeetingsForUser(VALID_TUTOR_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(meetingTO, result.get(0));
    }
}
