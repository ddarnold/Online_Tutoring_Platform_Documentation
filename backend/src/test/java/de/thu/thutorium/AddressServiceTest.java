package de.thu.thutorium;

import de.thu.thutorium.api.TOMappers.AddressTOMapper;
import de.thu.thutorium.api.transferObjects.common.AddressTO;
import de.thu.thutorium.api.transferObjects.common.UniversityTO;
import de.thu.thutorium.database.DBOMappers.AddressDBOMapper;
import de.thu.thutorium.database.dbObjects.AddressDBO;
import de.thu.thutorium.database.dbObjects.UniversityDBO;
import de.thu.thutorium.database.repositories.AddressRepository;
import de.thu.thutorium.database.repositories.UniversityRepository;
import de.thu.thutorium.services.implementations.AddressServiceImpl;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressTOMapper addressTOMapper;

    @Mock
    private AddressDBOMapper addressDBOMapper;

    @Mock
    private UniversityRepository universityRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private AddressTO addressTO;
    private AddressDBO addressDBO;
    private UniversityDBO universityDBO;

    @BeforeEach
    void setUp() {
        // Initialize test data with valid values
        UniversityTO universityTO = new UniversityTO();
        universityTO.setUniversityName("Test University");

        addressTO = new AddressTO();
        addressTO.setHouseNum("123");
        addressTO.setStreetName("Main Street");
        addressTO.setPostalCode("12345");
        addressTO.setCountry("Test Country");
        addressTO.setUniversity(universityTO);

        universityDBO = new UniversityDBO("Test University");
        addressDBO = new AddressDBO();
        addressDBO.setHouseNum("123");
        addressDBO.setStreetName("Main Street");
        addressDBO.setPostalCode("12345");
        addressDBO.setCountry("Test Country");
        addressDBO.setUniversity(universityDBO);
    }

    @Test
    void createUniversityAndAddress_Success() {
        // Mock repository and mapper behavior
        when(universityRepository.findByUniversityName(eq("Test University"))).thenReturn(Optional.of(universityDBO));
        when(addressRepository.findByHouseNumAndStreetNameIgnoreCaseAndPostalCodeAndCountryIgnoreCaseAndUniversity_UniversityNameIgnoreCase(
                eq("123"), eq("Main Street"), eq("12345"), eq("Test Country"), eq("Test University")
        )).thenReturn(Optional.empty());
        when(addressDBOMapper.toDBO(eq(addressTO))).thenReturn(addressDBO);
        when(addressRepository.save(any(AddressDBO.class))).thenReturn(addressDBO);
        when(addressTOMapper.toDTO(eq(addressDBO))).thenReturn(addressTO);

        // Call the service method
        AddressTO result = addressService.createUniversityAndAddress(addressTO);

        // Verify and assert
        assertNotNull(result);
        assertEquals(addressTO.getHouseNum(), result.getHouseNum());
        assertEquals(addressTO.getStreetName(), result.getStreetName());
        verify(universityRepository, times(1)).findByUniversityName(eq("Test University"));
        verify(addressRepository, times(1)).save(any(AddressDBO.class));
    }

    @Test
    void createUniversityAndAddress_UniversityDoesNotExist_Success() {
        // Mock repository and mapper behavior
        when(universityRepository.findByUniversityName(eq("Test University"))).thenReturn(Optional.empty());
        when(addressRepository.findByHouseNumAndStreetNameIgnoreCaseAndPostalCodeAndCountryIgnoreCaseAndUniversity_UniversityNameIgnoreCase(
                eq("123"), eq("Main Street"), eq("12345"), eq("Test Country"), eq("Test University")
        )).thenReturn(Optional.empty());
        when(addressDBOMapper.toDBO(eq(addressTO))).thenReturn(addressDBO);
        when(addressRepository.save(any(AddressDBO.class))).thenReturn(addressDBO);
        when(addressTOMapper.toDTO(eq(addressDBO))).thenReturn(addressTO);

        // Call the service method
        AddressTO result = addressService.createUniversityAndAddress(addressTO);

        // Verify and assert
        assertNotNull(result);
        assertEquals(addressTO.getUniversity().getUniversityName(), result.getUniversity().getUniversityName());
        verify(universityRepository, times(1)).findByUniversityName(eq("Test University"));
        verify(addressRepository, times(1)).save(any(AddressDBO.class));
    }

    @Test
    void createUniversityAndAddress_AlreadyExists_ThrowsException() {
        // Mock repository behavior
        when(universityRepository.findByUniversityName(eq("Test University"))).thenReturn(Optional.of(universityDBO));
        when(addressRepository.findByHouseNumAndStreetNameIgnoreCaseAndPostalCodeAndCountryIgnoreCaseAndUniversity_UniversityNameIgnoreCase(
                eq("123"), eq("Main Street"), eq("12345"), eq("Test Country"), eq("Test University")
        )).thenReturn(Optional.of(addressDBO));

        // Assert exception
        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                addressService.createUniversityAndAddress(addressTO)
        );

        assertEquals("Address already exists with the university Test University", exception.getMessage());
        verify(addressRepository, never()).save(any(AddressDBO.class));
    }
}
