package de.thu.thutorium;

import de.thu.thutorium.api.transferObjects.authentication.LogInRequestTO;
import de.thu.thutorium.api.transferObjects.authentication.LogInResponseTO;
import de.thu.thutorium.api.transferObjects.authentication.RegisterRequestTO;
import de.thu.thutorium.database.dbObjects.RoleDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.RoleRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.security.JwtService;
import de.thu.thutorium.services.implementations.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link AuthenticationServiceImpl}.
 * Contains unit tests for authentication and registration functionality.
 */
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class AuthenticationTests {

        @Test
        void testAuthenticate_Success() {
            // Arrange
            LogInRequestTO loginRequestTO = new LogInRequestTO("test@example.com", "password123");
            UserDetails mockUserDetails = mock(UserDetails.class);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null); // No exception thrown
            when(userDetailsService.loadUserByUsername(loginRequestTO.getEmail())).thenReturn(mockUserDetails);
            when(jwtService.generateToken(anyLong(), eq(mockUserDetails))).thenReturn("jwtToken");
            when(jwtService.getExpirationTime()).thenReturn(86400000L);

            // Act
            ResponseEntity<LogInResponseTO> response = authenticationService.authenticate(loginRequestTO);

            // Assert
            assertNotNull(response);
            assertEquals("jwtToken", response.getBody().getToken());
            assertNotNull(response.getBody().getCreatedAt());
            assertEquals(86400000L, response.getBody().getExpiresAt());

            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(userDetailsService, times(1)).loadUserByUsername(loginRequestTO.getEmail());
            verify(jwtService, times(1)).generateToken(anyLong(), eq(mockUserDetails));
        }
    }

    @Nested
    class RegistrationTests {

        @Test
        void testRegister_UserAlreadyExists() {
            // Arrange
            RegisterRequestTO registerRequestTO = new RegisterRequestTO(
                    "test@example.com",
                    Role.STUDENT, // Use the actual enum value here
                    "John",
                    "Doe",
                    "password123"
            );

            RoleDBO role = new RoleDBO(); // Mocked role object
            Set<RoleDBO> roles = new HashSet<>();
            roles.add(role);

            UserDBO existingUser = new UserDBO();
            existingUser.setEmail("test@example.com");
            existingUser.setRoles(roles);

            when(roleRepository.findByRoleName(Role.valueOf(registerRequestTO.role().name()))).thenReturn(role);
            when(userRepository.findByEmailAndRoles(registerRequestTO.email(), roles)).thenReturn(existingUser);

            // Act
            ResponseEntity<LogInResponseTO> response = authenticationService.register(registerRequestTO);

            // Assert
            assertNotNull(response);
            assertEquals(409, response.getStatusCodeValue());
            assertEquals("Email is already registered with the specified role", response.getBody().getToken());

            verify(roleRepository, times(1)).findByRoleName(Role.valueOf(registerRequestTO.role().name()));
            verify(userRepository, times(1))
                    .findByEmailAndRoles(registerRequestTO.email(), roles);
        }

        @Test
        void testRegister_NewUser() {
            // Arrange
            RegisterRequestTO registerRequestTO = new RegisterRequestTO(
                    "newuser@example.com",
                    Role.STUDENT, // Use the actual enum value here
                    "John",
                    "Doe",
                    "password123"
            );

            RoleDBO role = new RoleDBO(); // Mocked role object
            Set<RoleDBO> roles = new HashSet<>();
            roles.add(role);

            UserDBO newUser = new UserDBO();
            newUser.setEmail(registerRequestTO.email());
            newUser.setRoles(new HashSet<>());

            when(roleRepository.findByRoleName(Role.valueOf(registerRequestTO.role().name()))).thenReturn(role);
            when(userRepository.findByEmail(registerRequestTO.email())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(registerRequestTO.password())).thenReturn("encodedPassword");
            when(userRepository.save(any(UserDBO.class))).thenReturn(newUser);
            when(jwtService.generateToken(anyLong(), any(UserDetails.class))).thenReturn("jwtToken");
            when(jwtService.getExpirationTime()).thenReturn(86400000L);

            UserDetails mockUserDetails = mock(UserDetails.class);
            when(userDetailsService.loadUserByUsername(registerRequestTO.email())).thenReturn(mockUserDetails);

            // Act
            ResponseEntity<LogInResponseTO> response = authenticationService.register(registerRequestTO);

            // Assert
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertEquals("jwtToken", response.getBody().getToken());

            verify(roleRepository, times(1)).findByRoleName(Role.valueOf(registerRequestTO.role().name()));
            verify(userRepository, times(1)).findByEmail(registerRequestTO.email());
            verify(passwordEncoder, times(1)).encode(registerRequestTO.password());
            verify(userRepository, times(1)).save(any(UserDBO.class));
            verify(jwtService, times(1)).generateToken(anyLong(), any(UserDetails.class));
        }
    }
}