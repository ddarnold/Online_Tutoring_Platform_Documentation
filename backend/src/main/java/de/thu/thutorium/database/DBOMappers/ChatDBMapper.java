package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import de.thu.thutorium.database.dbObjects.ChatDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ChatDBMapper {
  public ChatDBO toEntity(ChatCreateTO dto, Set<UserDBO> participants) {
    return ChatDBO.builder()
        .participants(participants)
        .chatTitle(dto.getChatTitle())
        .isGroup(dto.getIsGroup())
        .build();
  }
}
