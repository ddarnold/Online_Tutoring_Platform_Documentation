package de.thu.thutorium.api.controllers;

import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import de.thu.thutorium.api.transferObjects.common.MessageTO;
import de.thu.thutorium.services.interfaces.ChatService;
import de.thu.thutorium.services.interfaces.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

/**
 * WebSocketController handles WebSocket messaging and facilitates real-time message sending through
 * WebSocket connections. This controller listens for messages sent from clients, saves them via the
 * message service, and broadcasts the messages to all subscribed clients.
 */
@RestController
public class WebSocketController {

  private final MessageService messageService;
  private final ChatService chatService;

  /**
   * Constructor for initializing the WebSocketController with the MessageService.
   *
   * @param messageService the service responsible for handling message operations
   */
  public WebSocketController(MessageService messageService, ChatService chatService) {
    this.messageService = messageService;
      this.chatService = chatService;
  }

  /**
   * Handles the sending of a new message via WebSocket. This method listens for messages sent to
   * the "/sendMessage" destination, processes the message, saves it using the message service, and
   * then broadcasts the message to all clients subscribed to the "/topic/messages" destination.
   *
   * @param messageTO the message data transfer object containing the message details
   * @return the saved message as a MessageDTO, which will be sent to all subscribers
   */

  @MessageMapping("/sendMessage")
  @SendTo("/topic/messages")
  public MessageTO sendMessage(MessageTO messageTO) {
    // Save the message via the service
    return messageService.saveMessage(messageTO);
  }

  /**
   * Sends a new message. This will persist the message in the database and send it to the
   * recipient.
   *
   * @param messageTO the message transfer object containing the message details
   * @return the created message wrapped in a ResponseEntity
   */
  @Operation(
          summary = "Send a new message",
          description = "Persists a new message in the database and sends it to the recipient.",
          tags = {"Message Operations"}
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Message sent successfully",
                  content = @Content(schema = @Schema(implementation = MessageTO.class))),
          @ApiResponse(responseCode = "400", description = "Invalid message data")
  })
  @PostMapping("/message/send")
  public ResponseEntity<MessageTO> PostsendMessage(@RequestBody MessageTO messageTO) {
    MessageTO savedMessage = messageService.saveMessage(messageTO);
    return ResponseEntity.ok(savedMessage);
  }

  /**
   * Creates a new chat.
   *
   * @param requestDTO the {@link ChatCreateTO} object containing chat details.
   * @return a success message.
   */
  @Operation(
          summary = "Create a new chat",
          description = "Creates a new chat session based on the provided chat details.",
          tags = {"Chat Operations"}
  )
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Chat created successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid chat data")
  })
  @PostMapping("/chat-create")
  public ResponseEntity<String> createChat(@RequestBody @Valid ChatCreateTO requestDTO) {
    chatService.createChat(requestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("Chat created successfully!");
  }

  /**
   * Deletes a chat by its ID.
   *
   * @param chatId the ID of the chat to delete.
   * @return a success message.
   */
  @Operation(
          summary = "Delete a chat by ID",
          description = "Deletes an existing chat by its unique ID.",
          tags = {"Chat Operations"}
  )
  @ApiResponses({
          @ApiResponse(responseCode = "204", description = "Chat deleted successfully"),
          @ApiResponse(responseCode = "404", description = "Chat not found")
  })
  @DeleteMapping("/chat-delete/{chatId}")
  public ResponseEntity<String> deleteChat(@PathVariable Long chatId) {
    chatService.deleteChat(chatId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Chat deleted successfully!");
  }
}
