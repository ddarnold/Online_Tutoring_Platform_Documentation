package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.MeetingTO;
import de.thu.thutorium.database.dbObjects.MeetingDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper interface for converting between {@link MeetingDBO} and {@link MeetingTO}.
 *
 * <p>This interface uses MapStruct to map fields between the entity and transfer object
 * representations of a meeting. The mapping is bidirectional, enabling conversion from a database
 * object to a transfer object for API responses.
 *
 * <p>The mapper is configured to be a Spring component using {@code componentModel = "spring"}.
 */
@Mapper(componentModel = "spring")
public interface MeetingTOMapper {

  /**
   * Converts a {@link MeetingDBO} entity to a {@link MeetingTO} transfer object.
   *
   * <p>Custom field mappings:
   *
   * <ul>
   *   <li>{@code tutor.userId} is mapped to {@code tutorId}
   *   <li>{@code course.courseId} is mapped to {@code courseId}
   *   <li>{@code address.addressId} is mapped to {@code addressId}
   *   <li>{@code meetingDate} is formatted as {@code yyyy-MM-dd}
   *   <li>{@code meetingTime}, and {@code roomNum} are mapped directly
   * </ul>
   *
   * @param meetingDBO the {@link MeetingDBO} entity to convert
   * @return a {@link MeetingTO} transfer object with mapped fields
   */
  @Mapping(source = "tutor.userId", target = "tutorId")
  @Mapping(source = "course.courseId", target = "courseId")
  @Mapping(source = "address.addressId", target = "addressId")
  @Mapping(source = "meetingDate", target = "meetingDate", dateFormat = "yyyy-MM-dd")
  @Mapping(source = "meetingTime", target = "meetingTime")
  @Mapping(source = "endTime", target = "endTime")
  @Mapping(source = "roomNum", target = "roomNum")
  MeetingTO toDTO(MeetingDBO meetingDBO);

  /**
   * Converts a list of {@link MeetingDBO} entities to a list of {@link MeetingTO} transfer objects.
   *
   * @param meetingDBOs the list of {@link MeetingDBO} entities to convert
   * @return a list of {@link MeetingTO} transfer objects with mapped fields
   */
  List<MeetingTO> toDTOList(List<MeetingDBO> meetingDBOs);
}
