package de.thu.thutorium.services.interfaces;

import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import org.springframework.stereotype.Service;

/**
 * ChatService is an interface for managing chat operations.
 *
 * <p>This service provides methods to create and delete chats within the system.
 */
@Service
public interface ChatService {
  /**
   * Creates a new chat based on the provided chat creation data.
   *
   * @param requestDTO the {@link ChatCreateTO} object containing the necessary information to
   *     create a chat.
   */
  void createChat(ChatCreateTO requestDTO);

  /**
   * Deletes a chat based on the provided chat ID.
   *
   * @param chatId the unique identifier of the chat to be deleted.
   */
  void deleteChat(Long chatId);
}
