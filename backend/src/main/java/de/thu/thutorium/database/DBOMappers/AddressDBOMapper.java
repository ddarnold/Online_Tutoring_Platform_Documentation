package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.AddressTO;
import de.thu.thutorium.database.dbObjects.AddressDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/** A MapStruct mapper interface for converting {@link AddressTO} to {@link AddressDBO}. */
@Component
@Mapper(
    componentModel = "spring",
    uses = {UniversityDBOMapper.class})
public interface AddressDBOMapper {
  /**
   * Converts an {@link AddressTO} object to an {@link AddressDBO} object.
   *
   * @param addressTO the {@code AdminAddressTO} object to convert
   * @return an {@code AddressDBO} object containing the mapped data
   */
  @Mapping(source = "university", target = "university")
  AddressDBO toDBO(AddressTO addressTO);
}
