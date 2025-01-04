package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import de.thu.thutorium.database.DBOMappers.ChatDBMapper;
import de.thu.thutorium.database.dbObjects.ChatDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.ChatRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.ChatService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
}
