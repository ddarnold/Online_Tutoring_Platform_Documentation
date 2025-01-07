package de.thu.thutorium.api.transferObjects.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSummaryTO {
  private Long chatId;
  private ReceiverTO receiver;
  private int unreadMessages;
}
