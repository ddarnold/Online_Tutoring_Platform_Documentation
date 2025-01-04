package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.AddressTO;
import de.thu.thutorium.database.dbObjects.AddressDBO;
import org.mapstruct.Mapper;

/** Mapper interface for converting {@link AddressDBO} to {@link AddressTO}. */
@Mapper(
    componentModel = "spring",
    uses = {UniversityTOMapper.class})
public interface AddressTOMapper {
  /**
   * Converts an {@link AddressDBO} object to an {@link AddressTO} object.
   *
   * @param addressDBO the {@code AddressDBO} object to convert
   * @return an {@code AdminAddressTO} object containing the mapped data
   */
  AddressTO toDTO(AddressDBO addressDBO);
}
