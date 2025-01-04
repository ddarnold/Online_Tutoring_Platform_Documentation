package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.UserTO;
import de.thu.thutorium.database.dbObjects.*;
import org.mapstruct.*;

import java.util.List;

/**
 * A MapStruct mapper interface for converting between {@link UserDBO} and {@link UserTO}.
 *
 * <p>This interface defines the mapping methods for converting a {@code UserDBO} (Database Object)
 * to a {@code UserBaseDTO} (Data Transfer Object), and vice versa. The mapper uses MapStruct to
 * automate the mapping process, ensuring efficient and type-safe conversion between the two
 * representations.
 *
 * <p>The methods in this interface handle single and list-based conversions. The {@code toDTO}
 * method converts a single {@code UserDBO} into a {@code UserBaseDTO}, while the {@code toDTOList}
 * method converts a list of {@code UserDBO} objects into a list of {@code UserBaseDTO}.
 *
 * <p>Note: The {@code componentModel = "spring"} annotation indicates that MapStruct will generate
 * a Spring bean for this mapper, allowing it to be injected into other components or services
 * within the application.
 */
@Mapper(componentModel = "spring", uses = {AffiliationTOMapper.class})
public interface UserTOMapper {
  /**
   * Converts a {@link UserDBO} object to a {@link UserTO}.
   *
   * @param user the {@code UserDBO} object to convert
   * @return a {@code UserBaseDTO} object containing the user data
   */
  UserTO toDTO(UserDBO user);

  /**
   * Converts a list of {@link UserDBO} objects to a list of {@link UserTO} objects.
   *
   * @param users a list of {@code UserDBO} objects to convert
   * @return a list of {@code UserBaseDTO} objects containing the user data
   */
  List<UserTO> toDTOList(List<UserDBO> users);
}
