package de.thu.thutorium.database.DBOMappers;

import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between {@link de.thu.thutorium.api.transferObjects.common.MeetingTO}
 * and {@link de.thu.thutorium.database.dbObjects.MeetingDBO}.
 *
 * <p>This interface uses MapStruct to map data transfer objects (DTOs) to database objects (DBOs)
 * and vice versa. The mapping configuration specifies how certain fields should be handled, such as
 * ignoring unmapped fields.
 *
 * <p>Annotations used:
 *
 * <ul>
 *   <li>@Mapper: Marks this interface as a MapStruct mapper.
 *   <li>componentModel = "spring": Configures the mapper to be a Spring-managed bean.
 *   <li>unmappedTargetPolicy = ReportingPolicy.IGNORE: Ignores unmapped target properties to avoid
 *       build errors.
 *   <li>@Mapping: Customizes the mapping of specific fields.
 * </ul>
 */
@Mapper(componentModel = "spring")
@Deprecated //Probably not used anywhere
public interface MeetingDBMapper {
//
//  /**
//   * Maps a {@link MeetingTO} object to a {@link MeetingDBO} object.
//   *
//   * @param meetingTO the {@link MeetingTO} object to map.
//   * @return the mapped {@link MeetingDBO} object.
//   */
//  @Mapping(target = "tutor", ignore = true) // Will be set in service layer
//  @Mapping(target = "course", ignore = true) // Will be set in service layer
//  @Mapping(target = "address", ignore = true) // Will be set in service layer
//  @Mapping(target = "participants", ignore = true) // Will be set in service layer
//  @Mapping(target = "meetingId", ignore = true) // Auto-generated in the database
//  @Mapping(source = "roomNum", target = "roomNum")
//  @Mapping(source = "meetingDate", target = "meetingDate")
//  @Mapping(source = "startTime", target = "startTime")
//  @Mapping(source = "endTime", target = "endTime")
//  @Mapping(source = "duration", target = "duration")
//  @Mapping(source = "meetingType", target = "meetingType")
//  MeetingDBO toEntity(MeetingTO meetingTO);
}
