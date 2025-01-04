package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for creating a chat.
 *
 * <p>This class is used to transfer data required to create a chat, including participants,
 * optional group title, and whether the chat is a group chat.
 *
 * <p>Annotations used:
 *
 * <ul>
 *   <li>@Data: Generates getter, setter, equals, hashCode, and toString methods.
 *   <li>@NoArgsConstructor: Generates a no-argument constructor.
 *   <li>@AllArgsConstructor: Generates a constructor with arguments for all fields.
 *   <li>@NotNull: Validates that the field is not null.
 *   <li>@Size: Validates the size of the collection (minimum of 2 participants).
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatCreateTO {
  /**
   * List of user IDs participating in the chat. This field cannot be null and must contain at least
   * two participants.
   */
  @NotNull
  @Size(min = 2, message = "A chat must have at least two participants.")
  private List<Long> participantIds;

  /** Optional title for group chats. Can be null or empty for non-group chats. */
  private String chatTitle;

  /** Boolean flag indicating if the chat is a group chat. This field cannot be null. */
  @NotNull private Boolean isGroup;
}
