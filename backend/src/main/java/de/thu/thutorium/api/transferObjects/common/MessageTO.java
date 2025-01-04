package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Transfer object representing a message.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageTO {

    /**
     * Unique identifier for the message.
     */
    private Long messageId;

    /**
     * The sender of the message.
     */
    @NotNull(message = "Sender cannot be null")
    private Long senderId;

    /**
     * The receiver of the message.
     */
    @NotNull(message = "Receiver cannot be null")
    private Long receiverId;

    @NotNull(message = "Receiver cannot be null")
    private Long chatId;

    /**
     * The content of the message.
     */
    @NotEmpty(message = "Message content cannot be empty")
    private String messageContent;

    /**
     * The timestamp when the message was sent.
     */
    @NotNull(message = "Send timestamp cannot be null")
    private LocalDateTime sendAt;

    /**
     * The timestamp when the message was read.
     */
    private LocalDateTime readAt;

    /**
     * Indicates whether the message has been read.
     */
    @NotNull(message = "Read status cannot be null")
    private Boolean isRead = false;

}
