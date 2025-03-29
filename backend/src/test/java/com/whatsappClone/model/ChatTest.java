package com.whatsappClone.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.whatsappClone.Model.Chat;
import com.whatsappClone.Model.Message;
import com.whatsappClone.Model.User;

public class ChatTest {

    private Chat chat;
    private User user1;
    private User user2;
    private Message message1;
    private Message message2;

    @BeforeEach
    public void setup() {
        // Initializing mock users and messages
        user1 = new User(); // Assuming you have a User constructor
        user2 = new User(); // Assuming you have a User constructor

        // Initializing messages
        message1 = new Message(); // Assuming you have a Message constructor
        message2 = new Message(); // Assuming you have a Message constructor

        // Initializing Chat instance
        Set<User> admins = new HashSet<>(Arrays.asList(user1));
        Set<User> users = new HashSet<>(Arrays.asList(user1, user2));
        List<Message> messages = Arrays.asList(message1, message2);

        chat = new Chat(1, "Test Chat", "chat_image.png", true, admins, user1, users, messages);
    }

    @Test
    public void testGetId() {
        assertEquals(1, chat.getId());
    }

    @Test
    public void testGetChatName() {
        assertEquals("Test Chat", chat.getChatName());
    }

    @Test
    public void testGetChatImage() {
        assertEquals("chat_image.png", chat.getChatImage());
    }

    @Test
    public void testIsGroup() {
        assertTrue(chat.isGroup());
    }

    @Test
    public void testGetAdmins() {
        Set<User> admins = chat.getAdmins();
        assertTrue(admins.contains(user1));
        assertEquals(1, admins.size());
    }

    @Test
    public void testGetCreatedBy() {
        assertEquals(user1, chat.getCreatedBy());
    }

    @Test
    public void testGetUsers() {
        Set<User> users = chat.getUsers();
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertEquals(2, users.size());
    }

    @Test
    public void testGetMessages() {
        List<Message> messages = chat.getMessages();
        assertTrue(messages.contains(message1));
        assertTrue(messages.contains(message2));
        assertEquals(2, messages.size());
    }

    @Test
    public void testSetId() {
        chat.setId(2);
        assertEquals(2, chat.getId());
    }

    @Test
    public void testSetChatName() {
        chat.setChatName("Updated Chat Name");
        assertEquals("Updated Chat Name", chat.getChatName());
    }

    @Test
    public void testSetChatImage() {
        chat.setChatImage("updated_image.png");
        assertEquals("updated_image.png", chat.getChatImage());
    }

    @Test
    public void testSetGroup() {
        chat.setGroup(false);
        assertFalse(chat.isGroup());
    }

    @Test
    public void testSetAdmins() {
        Set<User> newAdmins = new HashSet<>(Arrays.asList(user2));
        chat.setAdmins(newAdmins);
        assertTrue(chat.getAdmins().contains(user2));
    }

    @Test
    public void testSetCreatedBy() {
        chat.setCreatedBy(user2);
        assertEquals(user2, chat.getCreatedBy());
    }

    @Test
    public void testSetUsers() {
        Set<User> newUsers = new HashSet<>(Arrays.asList(user1, user2));
        chat.setUsers(newUsers);
        assertTrue(chat.getUsers().contains(user2));
    }

    @Test
    public void testSetMessages() {
        List<Message> newMessages = Arrays.asList(message2);
        chat.setMessages(newMessages);
        assertEquals(1, chat.getMessages().size());
    }

    @Test
    public void testChatConstructor() {
        Set<User> admins = new HashSet<>(Arrays.asList(user1));
        Set<User> users = new HashSet<>(Arrays.asList(user1, user2));
        List<Message> messages = Arrays.asList(message1);

        Chat newChat = new Chat(3, "Constructor Chat", "constructor_image.png", false, admins, user1, users, messages);

        assertEquals(3, newChat.getId());
        assertEquals("Constructor Chat", newChat.getChatName());
        assertEquals("constructor_image.png", newChat.getChatImage());
        assertFalse(newChat.isGroup());
    }

    @Test
    public void testToString() {
        String expectedToString = "Chat [id=" + chat.getId() + ", chatName=" + chat.getChatName() + ", chatImage="
                + chat.getChatImage() + ", isGroup=" + chat.isGroup() + ", admins=" + chat.getAdmins() + ", createdBy="
                + chat.getCreatedBy() + ", users=" + chat.getUsers() + ", messages=" + chat.getMessages() + "]";

        assertEquals(expectedToString, chat.toString());
    }
}
