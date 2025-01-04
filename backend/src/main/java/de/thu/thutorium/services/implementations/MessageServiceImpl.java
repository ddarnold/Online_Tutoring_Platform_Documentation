package de.thu.thutorium.services.implementations;

import de.thu.thutorium.api.transferObjects.common.MessageTO;
import de.thu.thutorium.database.dbObjects.ChatDBO;
import de.thu.thutorium.database.dbObjects.MessageDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.ChatRepository;
import de.thu.thutorium.database.repositories.MessageRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.interfaces.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service implementation for managing messages within a chat system.
 *
 * <p>This service provides methods for saving a message, marking a message as read, and interacting
 * with the {@link MessageRepository}, {@link ChatRepository}, and {@link UserRepository} for
 * database operations.
 */
@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final ChatRepository chatRepository;
  private final UserRepository userRepository;

  public MessageServiceImpl(
      MessageRepository messageRepository,
      ChatRepository chatRepository,
      UserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.chatRepository = chatRepository;
    this.userRepository = userRepository;
  }

  /**
   * Saves a new message to the chat system.
   *
   * <p>This method validates the input {@link MessageTO}, retrieves the associated chat, sender,
   * and receiver from the database using the provided IDs, and creates a new message entity to be
   * stored in the database. It then maps the saved entity back into a {@link MessageTO}.
   *
   * @param messageTO the transfer object containing the message data to be saved
   * @return the saved message as a {@link MessageTO}
   * @throws IllegalArgumentException if the provided messageTO is null
   * @throws RuntimeException if the chat, sender, or receiver cannot be found based on the provided
   *     IDs
   */
  @Override
  @Transactional
  public MessageTO saveMessage(MessageTO messageTO) {
    // Validate input data
    if (messageTO == null) {
      throw new IllegalArgumentException("MessageTO cannot be null");
    }

    // Find the chat by ID (assuming messageTO contains a chat ID)
    ChatDBO chat =
        chatRepository
            .findById(messageTO.getChatId()) // Corrected to use chatId from MessageTO
            .orElseThrow(
                () -> new RuntimeException("Chat not found for ID: " + messageTO.getChatId()));

    // Find the sender by ID
    UserDBO sender =
        userRepository
            .findById(messageTO.getSenderId()) // Use senderId from MessageTO
            .orElseThrow(
                () -> new RuntimeException("Sender not found for ID: " + messageTO.getSenderId()));

    // Find the receiver by ID
    UserDBO receiver =
        userRepository
            .findById(messageTO.getReceiverId()) // Use receiverId from MessageTO
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Receiver not found for ID: " + messageTO.getReceiverId()));

    // Create a new message entity
    MessageDBO messageDBO =
        MessageDBO.builder()
            .chat(chat)
            .sender(sender)
            .receiver(receiver)
            .messageContent(messageTO.getMessageContent())
            .sendAt(messageTO.getSendAt()) // Use sendAt from MessageTO
            .isRead(false)
            .build();

    // Save the message to the database
    messageRepository.save(messageDBO);

    // Map the saved entity back to a DTO and return it
    return new MessageTO(
        messageDBO.getMessageId(),
        messageDBO.getSender().getUserId(),
        messageDBO.getReceiver().getUserId(),
        messageDBO.getChat().getChatId(), // Return the chatId from the saved message
        messageDBO.getMessageContent(),
        messageDBO.getSendAt(),
        messageDBO.getReadAt(),
        messageDBO.getIsRead());
  }

  /**
   * Marks a message as read and updates the read timestamp.
   *
   * <p>This method finds the message by ID, sets the message as read, updates the read timestamp,
   * and saves the updated message back to the database. It then returns the updated message as a
   * {@link MessageTO}.
   *
   * @param messageId the ID of the message to be marked as read
   * @return the updated message as a {@link MessageTO}
   * @throws RuntimeException if the message cannot be found based on the provided ID
   */
  @Override
  public MessageTO markAsRead(Long messageId) {
    // Find the message by ID
    MessageDBO messageDBO =
        messageRepository
            .findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found for ID: " + messageId));

    // Set the message as read
    messageDBO.setIsRead(true);
    messageDBO.setReadAt(LocalDateTime.now());

    // Save the updated message
    messageRepository.save(messageDBO);

    // Map the updated entity back to a DTO and return it
    return new MessageTO(
        messageDBO.getMessageId(),
        messageDBO.getSender().getUserId(), // Return the senderId
        messageDBO.getReceiver().getUserId(), // Return the receiverId
        messageDBO.getChat().getChatId(), // Return the chatId from the saved message
        messageDBO.getMessageContent(),
        messageDBO.getSendAt(),
        messageDBO.getReadAt(),
        messageDBO.getIsRead());
  }
}
