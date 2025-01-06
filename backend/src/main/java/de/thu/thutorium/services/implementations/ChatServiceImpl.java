package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.transferObjects.chat.ChatSummaryTO;
import de.thu.thutorium.api.transferObjects.chat.ReceiverTO;
import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import de.thu.thutorium.database.DBOMappers.ChatDBMapper;
import de.thu.thutorium.database.dbObjects.ChatDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.ChatRepository;
import de.thu.thutorium.database.repositories.MessageRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.ChatService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing chat creation and deletion.
 *
 * <p>This service provides functionality for creating and deleting chats. It interacts with the
 * {@link ChatRepository} to perform database operations on chats, and the {@link UserRepository} to
 * fetch participants of the chat. The {@link ChatDBMapper} is used to map data transfer objects
 * (DTO) to database objects (DBO).
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
  /** Repository for managing chats. */
  private final ChatRepository chatRepository;

  /** Repository for managing users, used to fetch participants of the chat. */
  private final UserRepository userRepository;

  /** Mapper for converting {@link ChatCreateTO} DTO to {@link ChatDBO} entity. */
  private final ChatDBMapper chatMapper;

  private final MessageRepository messageRepository;

  /**
   * Creates a new chat.
   *
   * <p>This method retrieves the participants of the chat from the database, validates them, maps
   * the provided {@link ChatCreateTO} to a {@link ChatDBO}, and then saves the chat to the
   * database.
   *
   * @param requestDTO the transfer object containing the data required to create the chat
   * @throws EntityNotFoundException if one or more participants are not found in the database
   */
  @Transactional
  @Override
  public void createChat(ChatCreateTO requestDTO) {
    // Fetch participants from the database
    Set<UserDBO> participants =
            new HashSet<>(userRepository.findAllById(requestDTO.getParticipantIds()));

    // Ensure all participants are valid
    if (participants.size() != requestDTO.getParticipantIds().size()) {
      throw new EntityNotFoundException("One or more participants not found.");
    }

    // Map DTO to Entity
    ChatDBO chatDBO = chatMapper.toEntity(requestDTO, participants);

    // Save Chat Entity
    chatRepository.save(chatDBO);
  }

  /**
   * Deletes a chat by its ID.
   *
   * <p>This method checks if the chat exists by its ID and, if found, deletes it from the database.
   * If the chat is not found, an {@link EntityNotFoundException} is thrown.
   *
   * @param chatId the ID of the chat to be deleted
   * @throws EntityNotFoundException if the chat with the given ID is not found
   */
  @Override
  @Transactional
  public void deleteChat(Long chatId) {
    ChatDBO chatDBO =
            chatRepository
                    .findById(chatId)
                    .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

    chatRepository.delete(chatDBO);
  }

  @Override
  public List<ChatSummaryTO> getChatSummaries(Long userId) {
    // Fetch all chats for the user (assuming participation list contains the user)
    List<ChatDBO> userChats = chatRepository.findByParticipants_UserId(userId);

    // Map chats to summaries
    return userChats.stream().map(chat -> {
      // Find the other participant (receiver) in one-on-one chat
      UserDBO receiver = chat.getParticipants().stream()
              .filter(participant -> !participant.getUserId().equals(userId))
              .findFirst()
              .orElse(null);

      // Count unread messages for the user in this chat
      int unreadMessages = messageRepository.countByChat_ChatIdAndReceiver_UserIdAndIsReadFalse(chat.getChatId(), userId);

      // Create DTO
      return new ChatSummaryTO(
              chat.getChatId(),
              receiver != null ? new ReceiverTO(receiver.getUserId(), receiver.getFirstName(), receiver.getLastName()) : null,
              unreadMessages
      );
    }).collect(Collectors.toList());
  }
}
