package de.thu.thutorium;

import de.thu.thutorium.api.controllers.AdminController;
import de.thu.thutorium.api.controllers.AuthenticationController;
import de.thu.thutorium.api.transferObjects.authentication.LogInRequestTO;
import de.thu.thutorium.api.transferObjects.authentication.LogInResponseTO;
import de.thu.thutorium.api.transferObjects.authentication.RegisterRequestTO;
import de.thu.thutorium.api.transferObjects.common.AddressTO;
import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import de.thu.thutorium.api.transferObjects.common.CourseCategoryTO;
import de.thu.thutorium.api.transferObjects.common.UniversityTO;
import de.thu.thutorium.exceptions.ResourceAlreadyExistsException;
import de.thu.thutorium.services.interfaces.*;
import jakarta.persistence.EntityExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ThutoriumApplicationTests {

    @Mock
    private AddressService addressService;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    //NOW TESTING CREATEUNIVERSITYANDADDRES
    @Test
    public void testCreateUniversityAndAddress_Success(){
        AddressTO addressTO = new AddressTO(); // Create a mock AddressTO object
        AddressTO createdAddressTO = new AddressTO(); // Simulate the created address object
        when(addressService.createUniversityAndAddress(addressTO)).thenReturn(createdAddressTO);

        // When
        ResponseEntity<?> response = adminController.createUniversityAndAddress(addressTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdAddressTO, response.getBody());
        verify(addressService, times(1)).createUniversityAndAddress(addressTO); // Verify that the service was called once
    }

    @Test
    public void testCreateUniversityAndAddress_Conflict() {
        // Given
        AddressTO addressTO = new AddressTO(); // Create a mock AddressTO object
        when(addressService.createUniversityAndAddress(addressTO))
                .thenThrow(new EntityExistsException("University already exists"));

        // When
        ResponseEntity<?> response = adminController.createUniversityAndAddress(addressTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("University already exists", response.getBody());
        verify(addressService, times(1)).createUniversityAndAddress(addressTO); // Verify that the service was called once
    }

    @Test
    public void testCreateUniversityAndAddress_InternalServerError() {
        // Given
        AddressTO addressTO = new AddressTO(); // Create a mock AddressTO object
        when(addressService.createUniversityAndAddress(addressTO)).thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResponseEntity<?> response = adminController.createUniversityAndAddress(addressTO);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error: Unexpected error", response.getBody());
        verify(addressService, times(1)).createUniversityAndAddress(addressTO); // Verify that the service was called once
    }

    //NOW TESTING DELETEUSER
    @Test
    public void testDeleteUser_Success() {
        // Given
        Long userId = 1L;

        // No exception is thrown by the mock userService
        doNothing().when(userService).deleteUser(userId);

        // When
        ResponseEntity<?> response = adminController.deleteUser(userId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).deleteUser(userId); // Verify the service call
    }

    @Test
    public void testDeleteUser_NotFound() {
        // Given
        Long userId = 1L;
        doThrow(new UsernameNotFoundException("User not found")).when(userService).deleteUser(userId);

        // When
        ResponseEntity<?> response = adminController.deleteUser(userId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        verify(userService, times(1)).deleteUser(userId); // Verify the service call
    }

    @Test
    public void testDeleteUser_InternalServerError() {
        // Given
        Long userId = 1L;
        doThrow(new RuntimeException("Unexpected error")).when(userService).deleteUser(userId);

        // When
        ResponseEntity<?> response = adminController.deleteUser(userId);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error: Unexpected error", response.getBody());
        verify(userService, times(1)).deleteUser(userId); // Verify the service call
    }

    //NOW TESTING THE CreateCourseCategory METHOD
    @Test
    public void testCreateCourseCategory_Success() {
        // Given
        CourseCategoryTO courseCategoryTO = new CourseCategoryTO();
        CourseCategoryTO createdCategoryTO = new CourseCategoryTO();
        when(categoryService.createCourseCategory(courseCategoryTO)).thenReturn(createdCategoryTO);

        // When
        ResponseEntity<?> response = adminController.createCourseCategory(courseCategoryTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCategoryTO, response.getBody());
        verify(categoryService, times(1)).createCourseCategory(courseCategoryTO); // Verify the service call
    }

    @Test
    public void testCreateCourseCategory_Conflict() {
        // Given
        CourseCategoryTO courseCategoryTO = new CourseCategoryTO();
        when(categoryService.createCourseCategory(courseCategoryTO))
                .thenThrow(new EntityExistsException("Category already exists"));

        // When
        ResponseEntity<?> response = adminController.createCourseCategory(courseCategoryTO);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Category already exists", response.getBody());
        verify(categoryService, times(1)).createCourseCategory(courseCategoryTO); // Verify the service call
    }

    @Test
    public void testCreateCourseCategory_InternalServerError() {
        // Given
        CourseCategoryTO courseCategoryTO = new CourseCategoryTO();
        when(categoryService.createCourseCategory(courseCategoryTO))
                .thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResponseEntity<?> response = adminController.createCourseCategory(courseCategoryTO);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error: Unexpected error", response.getBody());
        verify(categoryService, times(1)).createCourseCategory(courseCategoryTO); // Verify the service call
    }

    //NOW TESTING FOR REGISTER IN AUTHCONTROLLER CLASS
//    @Test
//    void testRegister_Success() {
//        // Given
//        RegisterRequestTO request = new RegisterRequestTO();
//        ResponseEntity<LogInResponseTO> mockResponse = new ResponseEntity<>(new LogInResponseTO(), HttpStatus.OK);
//        when(authService.register(request)).thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<?> response = authenticationController.register(request);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        verify(authService, times(1)).register(request);
//    }
//
//    @Test
//    void testRegister_Conflict() {
//        // Given
//        RegisterRequestTO request = new RegisterRequestTO();
//        when(authService.register(request))
//                .thenThrow(new ResourceAlreadyExistsException("User already exists"));
//
//        // When
//        ResponseEntity<?> response = authenticationController.register(request);
//
//        // Then
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//        assertEquals("Error: User already exists", response.getBody());
//        verify(authService, times(1)).register(request);
//    }
//
//    @Test
//    void testRegister_InternalServerError() {
//        // Given
//        RegisterRequestTO request = new RegisterRequestTO();
//        when(authService.register(request)).thenThrow(new RuntimeException("Unexpected error"));
//
//        // When
//        ResponseEntity<?> response = authenticationController.register(request);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Unexpected Error: Unexpected error", response.getBody());
//        verify(authService, times(1)).register(request);
//    }
//
//    //NOW TESTING AUTHENTICATION METHOD
//    @Test
//    void testAuthenticate_Success() {
//        // Given
//        LogInRequestTO request = new LogInRequestTO();
//        ResponseEntity<LogInResponseTO> mockResponse = new ResponseEntity<>(new LogInResponseTO(), HttpStatus.OK);
//        when(authService.authenticate(request)).thenReturn(mockResponse);
//
//        // When
//        ResponseEntity<?> response = authenticationController.authenticate(request);
//
//        // Then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        verify(authService, times(1)).authenticate(request);
//    }
//
//    @Test
//    void testAuthenticate_InternalServerError() {
//        // Given
//        LogInRequestTO request = new LogInRequestTO();
//        when(authService.authenticate(request)).thenThrow(new RuntimeException("Unexpected error"));
//
//        // When
//        ResponseEntity<?> response = authenticationController.authenticate(request);
//
//        // Then
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Unexpected Error: Unexpected error", response.getBody());
//        verify(authService, times(1)).authenticate(request);
//    }

}
