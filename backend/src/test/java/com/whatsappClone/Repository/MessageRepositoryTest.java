package com.whatsappClone.Repository;

import com.whatsappClone.Model.Message;
import com.whatsappClone.Model.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    private Chat chat;

    private Message message1, message2;

    @BeforeEach
    public void setUp() {
        chat = new Chat(); // Initialize the Chat object.
        chat.setId(1);

        message1 = new Message();
        message1.setId(1);
        message1.setContent("Hello");
        message1.setChat(chat);

        message2 = new Message();
        message2.setId(2);
        message2.setContent("Hi");
        message2.setChat(chat);

        // Save the messages in the repository.
        messageRepository.saveAll(Arrays.asList(message1, message2));
    }

    @Test
    public void testFindByChatId() {
        // Act
        List<Message> result = messageRepository.findByChatId(chat.getId());

        // Assert
        assertThat(result).hasSize(2); // Expecting two messages
        assertThat(result).contains(message1, message2);
    }
}
