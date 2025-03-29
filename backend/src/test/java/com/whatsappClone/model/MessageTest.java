package com.whatsappClone.model;

import com.whatsappClone.Model.Chat;
import com.whatsappClone.Model.Message;
import com.whatsappClone.Model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testNoArgsConstructor() {
        Message message = new Message();
        assertNotNull(message);
    }

    @Test
    public void testAllArgsConstructor() {
        LocalDateTime timestamp = LocalDateTime.now();
        Chat chat = new Chat();
        User user = new User();
        byte[] fileData = {1, 2, 3};

        Message message = new Message(1, "Hello World", fileData, "text/plain", "test.txt", timestamp, chat, user);

        assertEquals(1, message.getId());
        assertEquals("Hello World", message.getContent());
        assertArrayEquals(fileData, message.getFileData());
        assertEquals("text/plain", message.getFileType());
        assertEquals("test.txt", message.getFileName());
        assertEquals(timestamp, message.getTimestamp());
        assertEquals(chat, message.getChat());
        assertEquals(user, message.getUser());
    }

    @Test
    public void testSettersAndGetters() {
        Message message = new Message();

        LocalDateTime timestamp = LocalDateTime.now();
        Chat chat = new Chat();
        User user = new User();
        byte[] fileData = {1, 2, 3};

        message.setId(100);
        message.setContent("This is a test message");
        message.setFileData(fileData);
        message.setFileType("application/pdf");
        message.setFileName("document.pdf");
        message.setTimestamp(timestamp);
        message.setChat(chat);
        message.setUser(user);

        assertEquals(100, message.getId());
        assertEquals("This is a test message", message.getContent());
        assertArrayEquals(fileData, message.getFileData());
        assertEquals("application/pdf", message.getFileType());
        assertEquals("document.pdf", message.getFileName());
        assertEquals(timestamp, message.getTimestamp());
        assertEquals(chat, message.getChat());
        assertEquals(user, message.getUser());
    }

    @Test
    public void testToString() {
        LocalDateTime timestamp = LocalDateTime.now();
        Chat chat = new Chat();
        User user = new User();
        byte[] fileData = {1, 2, 3};

        Message message = new Message(1, "Hello World", fileData, "text/plain", "test.txt", timestamp, chat, user);
        String expected = "Message [id=1, content=Hello World, fileType=text/plain, fileName=test.txt, timestamp=" + timestamp + ", chat=" + chat + ", user=" + user + "]";

        assertEquals(expected, message.toString());
    }
}
