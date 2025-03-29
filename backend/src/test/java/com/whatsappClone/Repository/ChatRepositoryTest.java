package com.whatsappClone.Repository;

import com.whatsappClone.Model.Chat;
import com.whatsappClone.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;

    @BeforeEach
    @Sql(statements = {
            "INSERT INTO users (id, name, email, profile, password) VALUES (1, 'User1', 'user1@example.com', 'profile1.png', 'password1')",
            "INSERT INTO users (id, name, email, profile, password) VALUES (2, 'User2', 'user2@example.com', 'profile2.png', 'password2')",
            "INSERT INTO users (id, name, email, profile, password) VALUES (3, 'User3', 'user3@example.com', 'profile3.png', 'password3')",
            "INSERT INTO chats (id, is_group) VALUES (1, false)",
            "INSERT INTO chats (id, is_group) VALUES (2, true)",
            "INSERT INTO chat_users (chat_id, user_id) VALUES (1, 1), (1, 2), (2, 1), (2, 3)"
    })
    public void setup() {
        // The @Sql annotations prepare the test data in the database.
    }

    @Test
    public void testFindChatByUserId() {
        List<Chat> chats = chatRepository.findChatByUserId(1);

        assertNotNull(chats);
        assertEquals(2, chats.size());
    }

    @Test
    public void testFindSingleChatByUserIds() {
        User user1 = new User();
        user1.setId(1);

        User user2 = new User();
        user2.setId(2);

        Chat chat = chatRepository.findSingleChatByUserIds(user1, user2);

        assertNotNull(chat);
        assertFalse(chat.isGroup());
        assertEquals(1, chat.getId());
    }

    @Test
    public void testFindChatByUserId_noChatsFound() {
        List<Chat> chats = chatRepository.findChatByUserId(999);

        assertNotNull(chats);
        assertTrue(chats.isEmpty());
    }

    @Test
    public void testFindSingleChatByUserIds_noChatFound() {
        User user1 = new User();
        user1.setId(1);

        User user2 = new User();
        user2.setId(3); // Not part of the same chat.

        Chat chat = chatRepository.findSingleChatByUserIds(user1, user2);

        assertNull(chat);
    }
}
