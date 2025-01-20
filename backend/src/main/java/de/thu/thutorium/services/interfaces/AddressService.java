package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.AddressTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing addresses.
 *
 * <p>This interface defines methods for creating a new address and checking if an address already
 * exists in the system. The {@link AddressTO} transfer object is used for address data
 * representation and interaction with the service layer.
 */
@Service
public interface AddressService {
  /**
   * Creates a new university and associated address.
   *
   * @param address the {@code AddressTO} object containing the address data
   * @return the newly created {@code AddressTO} object
   */
  AddressTO createUniversityAndAddress(@Valid AddressTO address);

  List<AddressTO> getAddressesById(Long addressId);

  List<AddressTO> getAllAddresses();
}
