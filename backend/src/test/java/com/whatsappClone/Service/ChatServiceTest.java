package com.whatsappClone.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.whatsappClone.ServiceImpl.ChatServiceImpl;
import com.whatsappClone.ServiceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.whatsappClone.Exception.ChatException;
import com.whatsappClone.Exception.UserException;
import com.whatsappClone.Model.Chat;
import com.whatsappClone.Model.User;
import com.whatsappClone.Payload.GroupChatRequest;
import com.whatsappClone.Repository.ChatRepository;

class ChatServiceImplTest {

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ChatRepository chatRepository;

    private User user1;
    private User user2;
    private Chat existingChat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1);
        user1.setName("User1");

        user2 = new User();
        user2.setId(2);
        user2.setName("User2");

        existingChat = new Chat();
        existingChat.setId(1);
        existingChat.setGroup(false);
        existingChat.setCreatedBy(user1);
        existingChat.getUsers().add(user1);
        existingChat.getUsers().add(user2);
    }

    @Test
    void testCreateChatWhenChatExists() throws UserException {
        when(userService.findUserById(2)).thenReturn(user2);
        when(chatRepository.findSingleChatByUserIds(user2, user1)).thenReturn(existingChat);

        Chat chat = chatService.createChat(user1, 2);

        assertNotNull(chat);
        assertEquals(existingChat, chat);
        verify(chatRepository, never()).save(any());
    }

    @Test
    void testCreateChatWhenChatDoesNotExist() throws UserException {
        when(userService.findUserById(2)).thenReturn(user2);
        when(chatRepository.findSingleChatByUserIds(user2, user1)).thenReturn(null);

        Chat newChat = new Chat();
        newChat.setCreatedBy(user1);
        newChat.setGroup(false);
        newChat.getUsers().addAll(Arrays.asList(user1, user2));

        when(chatRepository.save(any(Chat.class))).thenReturn(newChat);

        Chat createdChat = chatService.createChat(user1, 2);

        assertNotNull(createdChat);
        assertEquals(user1, createdChat.getCreatedBy());
        assertEquals(2, createdChat.getUsers().size());
        assertFalse(createdChat.isGroup());
    }

    @Test
    void testFindChatByIdWhenExists() throws ChatException {
        when(chatRepository.findById(1)).thenReturn(Optional.of(existingChat));

        Chat chat = chatService.findChatById(1);

        assertNotNull(chat);
        assertEquals(1, chat.getId());
    }

    @Test
    void testFindChatByIdWhenNotExists() {
        when(chatRepository.findById(1)).thenReturn(Optional.empty());

        ChatException exception = assertThrows(ChatException.class, () -> {
            chatService.findChatById(1);
        });

        assertEquals("The requested chat is not found", exception.getMessage());
    }

    @Test
    void testCreateGroup() throws UserException {
        GroupChatRequest groupChatRequest = new GroupChatRequest();
        groupChatRequest.setChatName("Group1");
        groupChatRequest.setChatImage("image.png");
        groupChatRequest.setUserIds(Arrays.asList(2));

        when(userService.findUserById(2)).thenReturn(user2);

        Chat groupChat = new Chat();
        groupChat.setGroup(true);
        groupChat.setChatName("Group1");
        groupChat.getUsers().addAll(Arrays.asList(user1, user2));
        groupChat.getAdmins().add(user1);
        groupChat.setCreatedBy(user1);

        when(chatRepository.save(any(Chat.class))).thenReturn(groupChat);

        Chat createdGroup = chatService.createGroup(groupChatRequest, user1);

        assertNotNull(createdGroup);
        assertTrue(createdGroup.isGroup());
        assertEquals("Group1", createdGroup.getChatName());
        assertEquals(2, createdGroup.getUsers().size());
    }

    @Test
    void testAddUserToGroup() throws UserException, ChatException {
        User user3 = new User();
        user3.setId(3);
        user3.setName("User3");

        existingChat.getAdmins().add(user1);

        when(chatRepository.findById(1)).thenReturn(Optional.of(existingChat));
        when(userService.findUserById(3)).thenReturn(user3);

        Chat updatedChat = chatService.addUserToGroup(3, 1, user1);

        assertTrue(updatedChat.getUsers().contains(user3));
    }

    @Test
    void testRemoveFromGroup() throws UserException, ChatException {
        existingChat.getAdmins().add(user1);

        when(chatRepository.findById(1)).thenReturn(Optional.of(existingChat));
        when(userService.findUserById(2)).thenReturn(user2);

        Chat updatedChat = chatService.removeFromGroup(1, 2, user1);

        assertFalse(updatedChat.getUsers().contains(user2));
    }

    @Test
    void testDeleteChat() throws UserException, ChatException {
        when(chatRepository.findById(1)).thenReturn(Optional.of(existingChat));

        chatService.deleteChat(1, user1.getId());

        verify(chatRepository, times(1)).delete(existingChat);
    }
}
