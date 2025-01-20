package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.TOMappers.AddressTOMapper;
import de.thu.thutorium.api.transferObjects.common.AddressTO;
import de.thu.thutorium.database.DBOMappers.AddressDBOMapper;
import de.thu.thutorium.database.dbObjects.AddressDBO;
import de.thu.thutorium.database.dbObjects.UniversityDBO;
import de.thu.thutorium.database.repositories.AddressRepository;
import de.thu.thutorium.database.repositories.UniversityRepository;
import de.thu.thutorium.services.interfaces.AddressService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service implementation for managing addresses. */
@RequiredArgsConstructor
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;
  private final AddressTOMapper addressTOMapper;
  private final AddressDBOMapper addressDBOMapper;
  private final UniversityRepository universityRepository;

  /**
   * Creates a new university and associated address.
   *
   * <p>The address is always associated with a university. If the university does not exist, it is
   * created. If the university already exists, the new address is associated with the university as
   * an additional address.
   *
   * @param address the {@code AddressTO} object containing the address data
   * @return the newly created {@code AddressTO} object
   * @throws {@link de.thu.thutorium.exceptions.ResourceAlreadyExistsException} if the address
   *     already exists associated with the university
   */
  @Override
  @Transactional
  public AddressTO createUniversityAndAddress(@Valid AddressTO address) {
    // Check if the university already exists by name
    // If else, create a new one
    UniversityDBO universityDBO =
            universityRepository
                    .findByUniversityName(address.getUniversity().getUniversityName())
                    .orElseGet(() -> new UniversityDBO(address.getUniversity().getUniversityName()));

    // Check if the address already exists by address and university
    Optional<AddressDBO> resultAddressDBO =
            addressRepository
                    .findByHouseNumAndStreetNameIgnoreCaseAndPostalCodeAndCountryIgnoreCaseAndUniversity_UniversityNameIgnoreCase(
                            address.getHouseNum(),
                            address.getStreetName(),
                            address.getPostalCode(),
                            address.getCountry(),
                            address.getUniversity().getUniversityName());

    AddressDBO addressDBO;

    // Save newly created address and university, if it does not already exist.
    if (resultAddressDBO.isEmpty()) {
      addressDBO = addressDBOMapper.toDBO(address);
      addressDBO.setUniversity(universityDBO);
      AddressDBO savedAddress = addressRepository.save(addressDBO);
      return addressTOMapper.toDTO(savedAddress);
    } else {
      throw new EntityExistsException(
              "Address already exists with the university " + universityDBO.getUniversityName());
    }
  }

  @Override
  public List<AddressTO> getAddressesById(Long addressId) {
    List<AddressDBO> addressDBOs = addressRepository.findByAddressId(addressId);
    return addressDBOs.stream().map(addressTOMapper::toDTO).collect(Collectors.toList());
  }

  @Override
  public List<AddressTO> getAllAddresses() {
    return addressRepository.findAll()
            .stream()
            .map(addressTOMapper::toDTO)
            .collect(Collectors.toList());
  }
}
