package com.whatsappClone.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.whatsappClone.ServiceImpl.ChatServiceImpl;
import com.whatsappClone.ServiceImpl.MessageServiceImpl;
import com.whatsappClone.ServiceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.whatsappClone.Exception.ChatException;
import com.whatsappClone.Exception.MessageException;
import com.whatsappClone.Exception.UserException;
import com.whatsappClone.Model.Chat;
import com.whatsappClone.Model.Message;
import com.whatsappClone.Model.User;
import com.whatsappClone.Payload.SendMessageRequest;
import com.whatsappClone.Repository.MessageRepository;

class MessageServiceImplTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ChatServiceImpl chatService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private User user1;
    private Chat chat;
    private Message message;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1);
        user1.setName("User1");

        chat = new Chat();
        chat.setId(1);
        chat.setGroup(false);
        chat.getUsers().add(user1);

        message = new Message();
        message.setId(1);
        message.setChat(chat);
        message.setUser(user1);
        message.setContent("Hello");
        message.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testSendMessage() throws UserException, ChatException {
        SendMessageRequest request = new SendMessageRequest();
        request.setUserId(1);
        request.setChatId(1);
        request.setContent("Hello, this is a test message");

        when(userService.findUserById(1)).thenReturn(user1);
        when(chatService.findChatById(1)).thenReturn(chat);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message sentMessage = messageService.sendMessage(request);

        assertNotNull(sentMessage);
        assertEquals("Hello", sentMessage.getContent());
        verify(messagingTemplate, times(1)).convertAndSend("/user/1", sentMessage);
    }

    @Test
    void testGetChatsMessages() throws ChatException, UserException {
        when(chatService.findChatById(1)).thenReturn(chat);
        when(messageRepository.findByChatId(1)).thenReturn(Arrays.asList(message));

        List<Message> messages = messageService.getChatsMessages(1, user1);

        assertNotNull(messages);
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getContent());
    }

    @Test
    void testGetChatsMessagesUnauthorized() throws ChatException, UserException {
        User user2 = new User();
        user2.setId(2);

        when(chatService.findChatById(1)).thenReturn(chat);

        UserException exception = assertThrows(UserException.class, () -> {
            messageService.getChatsMessages(1, user2);
        });

        assertEquals("You are not related to this chat", exception.getMessage());
    }

    @Test
    void testFindMessageByIdWhenExists() throws MessageException {
        when(messageRepository.findById(1)).thenReturn(Optional.of(message));

        Message foundMessage = messageService.findMessageById(1);

        assertNotNull(foundMessage);
        assertEquals(1, foundMessage.getId());
        assertEquals("Hello", foundMessage.getContent());
    }

    @Test
    void testFindMessageByIdWhenNotExists() {
        when(messageRepository.findById(1)).thenReturn(Optional.empty());

        MessageException exception = assertThrows(MessageException.class, () -> {
            messageService.findMessageById(1);
        });

        assertEquals("The required message is not found", exception.getMessage());
    }

    @Test
    void testDeleteMessageByOwner() throws MessageException {
        when(messageRepository.findById(1)).thenReturn(Optional.of(message));

        messageService.deleteMessage(1, user1);

        verify(messageRepository, times(1)).delete(message);
    }

    @Test
    void testDeleteMessageByNonOwner() throws MessageException {
        User user2 = new User();
        user2.setId(2);

        when(messageRepository.findById(1)).thenReturn(Optional.of(message));

        MessageException exception = assertThrows(MessageException.class, () -> {
            messageService.deleteMessage(1, user2);
        });

        assertEquals("You are not authorized for this task", exception.getMessage());
    }
}
