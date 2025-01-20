package de.thu.thutorium.api.TOMappers;

import de.thu.thutorium.api.transferObjects.common.MessageTO;
import de.thu.thutorium.database.dbObjects.MessageDBO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageTOMapper {
    @Mapping(source = "sender.userId", target = "senderId")
    @Mapping(source = "receiver.userId", target = "receiverId")
    @Mapping(source = "chat.chatId", target = "chatId")
    MessageTO toDTO(MessageDBO messageDBO);

    List<MessageTO> toDTOList(List<MessageDBO> messageDBOList);
}
