package de.thu.thutorium;

import de.thu.thutorium.api.transferObjects.common.ChatCreateTO;
import de.thu.thutorium.database.DBOMappers.ChatDBMapper;
import de.thu.thutorium.database.dbObjects.ChatDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.repositories.ChatRepository;
import de.thu.thutorium.database.repositories.MessageRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.services.implementations.ChatServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatDBMapper chatMapper;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    private UserDBO user1;
    private UserDBO user2;
    private ChatCreateTO chatCreateTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock users
        user1 = new UserDBO();
        user1.getUserId();
        user1.setFirstName("John");
        user1.setLastName("Doe");

        user2 = new UserDBO();
        user2.getUserId();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");

        // Initialize ChatCreateTO with participants
        chatCreateTO = new ChatCreateTO();
        chatCreateTO.setParticipantIds(Arrays.asList(1L, 2L));
    }

    @Test
    public void testCreateChat_Success() {
        // Given
        Set<UserDBO> participants = new HashSet<>(Arrays.asList(user1, user2));
        ChatDBO chatDBO = new ChatDBO();

        // When
        when(userRepository.findAllById(chatCreateTO.getParticipantIds())).thenReturn(new ArrayList<>(participants));
        when(chatMapper.toEntity(chatCreateTO, participants)).thenReturn(chatDBO);

        // Call the method under test
        chatService.createChat(chatCreateTO);

        // Then
        verify(chatRepository, times(1)).save(chatDBO);
    }

    @Test
    public void testCreateChat_ParticipantNotFound() {
        // Given: Empty participant list from the repository
        when(userRepository.findAllById(chatCreateTO.getParticipantIds())).thenReturn(Collections.emptyList());

        // Then: EntityNotFoundException should be thrown
        assertThrows(EntityNotFoundException.class, () -> chatService.createChat(chatCreateTO));

        // Verify no interaction with the repository save method
        verify(chatRepository, never()).save(any(ChatDBO.class));
    }

    @Test
    public void testDeleteChat_Success() {
        // Given
        Long chatId = 1L;
        ChatDBO chatDBO = new ChatDBO();
        chatDBO.setChatId(chatId);

        // When: Chat is found in the repository
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chatDBO));

        // Call the method under test
        chatService.deleteChat(chatId);

        // Then: Ensure delete is called once on the repository
        verify(chatRepository, times(1)).delete(chatDBO);
    }

    @Test
    public void testDeleteChat_ChatNotFound() {
        // Given: Chat is not found in the repository
        Long chatId = 1L;
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // Then: EntityNotFoundException should be thrown
        assertThrows(EntityNotFoundException.class, () -> chatService.deleteChat(chatId));

        // Verify no interaction with the repository delete method
        verify(chatRepository, never()).delete(any(ChatDBO.class));
    }

    @Test
    public void testCreateChat_InvalidParticipantData() {
        // Given: A participant with a null ID (invalid data)
        chatCreateTO.setParticipantIds(Arrays.asList(1L, null));

        // When: Invalid data is passed for participants
        when(userRepository.findAllById(chatCreateTO.getParticipantIds()))
                .thenReturn(Arrays.asList(user1, null));

        // Then: EntityNotFoundException should be thrown due to null participant
        assertThrows(EntityNotFoundException.class, () -> chatService.createChat(chatCreateTO));
    }

    @Test
    public void testDeleteChat_AlreadyDeleted() {
        // Given: Attempt to delete an already deleted chat
        Long chatId = 1L;
        ChatDBO chatDBO = new ChatDBO();
        chatDBO.setChatId(chatId);

        // When: Chat is found but delete is called twice
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chatDBO));

        chatService.deleteChat(chatId);
        chatService.deleteChat(chatId); // Second call for already deleted chat

        // Then: Ensure delete is called once
        verify(chatRepository, times(1)).delete(chatDBO);
    }
}
