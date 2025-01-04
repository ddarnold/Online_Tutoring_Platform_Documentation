package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.MessageTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code MessageService} interface provides methods for managing messages.
 *
 * <p>It exposes the following functionalities:
 *
 * <ul>
 *   <li>Save a new message.
 *   <li>Mark a message as read.
 * </ul>
 */
@Service
public interface MessageService {
  /**
   * Saves a new message.
   *
   * <p>This method saves the provided message data into the system.
   *
   * @param messageTO the {@link MessageTO} object containing the data of the message to be saved.
   * @return the saved {@link MessageTO} object with any modifications (e.g., ID assignment).
   */
  MessageTO saveMessage(MessageTO messageTO);

  /**
   * Marks a message as read.
   *
   * <p>This method marks the message with the specified ID as read in the system.
   *
   * @param messageId the unique ID of the message to be marked as read.
   * @return the updated {@link MessageTO} object with the message marked as read.
   */
  void markAsRead(Long messageId);

  List<MessageTO> getMessagesByChatId(Long chatId);

}
