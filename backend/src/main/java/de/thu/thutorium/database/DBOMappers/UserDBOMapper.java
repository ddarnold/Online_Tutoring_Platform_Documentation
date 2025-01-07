package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.UserTO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import org.mapstruct.Mapper;

/** A MapStruct mapper interface for converting {@link UserDBO} to {@link UserTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AffiliationDBOMapper.class})
public interface UserDBOMapper {
  /**
   * Converts a {@link UserTO} object to a {@link UserDBO}.
   *
   * @param user the {@code UserTO} object to convert
   * @return a {@code UserDBO} object containing the user data
   */
  UserDBO toDBO(UserTO user);
}
