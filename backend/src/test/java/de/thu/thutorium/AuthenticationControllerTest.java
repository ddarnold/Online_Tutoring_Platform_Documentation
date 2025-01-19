package de.thu.thutorium;

import de.thu.thutorium.api.controllers.AuthenticationController;
import de.thu.thutorium.api.transferObjects.authentication.LogInRequestTO;
import de.thu.thutorium.api.transferObjects.authentication.LogInResponseTO;
import de.thu.thutorium.api.transferObjects.authentication.RegisterRequestTO;
import de.thu.thutorium.exceptions.ResourceAlreadyExistsException;
import de.thu.thutorium.services.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import de.thu.thutorium.database.dbObjects.enums.Role;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void testRegister_Success() {
        // Given
        RegisterRequestTO request = new RegisterRequestTO(
                "test@example.com",  // email
                Role.TUTOR,           // role
                "John",              // firstName
                "Doe",               // lastName
                "password123"        // password
        );
        ResponseEntity<LogInResponseTO> mockResponse = new ResponseEntity<>(new LogInResponseTO(), HttpStatus.OK);
        when(authService.register(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<?> response = authenticationController.register(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testRegister_Conflict() {
        // Given
        RegisterRequestTO request = new RegisterRequestTO(
                "test@example.com",
                Role.TUTOR,
                "John",
                "Doe",
                "password123"
        );
        when(authService.register(request))
                .thenThrow(new ResourceAlreadyExistsException("User already exists"));

        // When
        ResponseEntity<?> response = authenticationController.register(request);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error: User already exists", response.getBody());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testRegister_InternalServerError() {
        // Given
        RegisterRequestTO request = new RegisterRequestTO(
                "test@example.com",
                Role.TUTOR,
                "John",
                "Doe",
                "password123"
        );
        when(authService.register(request)).thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResponseEntity<?> response = authenticationController.register(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected Error: Unexpected error", response.getBody());
        verify(authService, times(1)).register(request);
    }

    @Test
    void testAuthenticate_Success() {
        // Given
        LogInRequestTO request = LogInRequestTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();
        ResponseEntity<LogInResponseTO> mockResponse = new ResponseEntity<>(new LogInResponseTO(), HttpStatus.OK);
        when(authService.authenticate(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<?> response = authenticationController.authenticate(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authService, times(1)).authenticate(request);
    }

    @Test
    void testAuthenticate_InternalServerError() {
        // Given
        LogInRequestTO request = LogInRequestTO.builder()
                .email("test@example.com")
                .password("password123")
                .build();
        when(authService.authenticate(request)).thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResponseEntity<?> response = authenticationController.authenticate(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected Error: Unexpected error", response.getBody());
        verify(authService, times(1)).authenticate(request);
    }

    private AuthenticationController verify(AuthenticationService authService, VerificationMode times) {
        return null;
    }

}
