package com.whatsappClone.model;

import com.whatsappClone.Model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    public void testAllArgsConstructor() {
        User user = new User(1, "John Doe", "john.doe@example.com", "default.png", "securepassword");

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("default.png", user.getProfile());
        assertEquals("securepassword", user.getPassword());
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();

        user.setId(10);
        user.setName("Alice Smith");
        user.setEmail("alice.smith@example.com");
        user.setProfile("profile.jpg");
        user.setPassword("anotherpassword");

        assertEquals(10, user.getId());
        assertEquals("Alice Smith", user.getName());
        assertEquals("alice.smith@example.com", user.getEmail());
        assertEquals("profile.jpg", user.getProfile());
        assertEquals("anotherpassword", user.getPassword());
    }

    @Test
    public void testToString() {
        User user = new User(2, "Bob Johnson", "bob.johnson@example.com", "avatar.png", "mypassword");
        String expected = "User [id=2, name=Bob Johnson, email=bob.johnson@example.com, profile=avatar.png, password=mypassword]";

        assertEquals(expected, user.toString());
    }
}
