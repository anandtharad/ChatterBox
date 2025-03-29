package com.whatsappClone.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.whatsappClone.ServiceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;

import com.whatsappClone.Exception.UserException;
import com.whatsappClone.Model.User;
import com.whatsappClone.Payload.UpdateUserRequest;
import com.whatsappClone.Repository.UserRepository;
import com.whatsappClone.config.TokenProvider;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User();
        user1.setId(1);
        user1.setName("User1");
        user1.setEmail("user1@example.com");
    }

    @Test
    void testFindUserByIdSuccess() throws UserException {
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        User foundUser = userService.findUserById(1);

        assertNotNull(foundUser);
        assertEquals("User1", foundUser.getName());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            userService.findUserById(1);
        });

        assertEquals("The requested user is not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFindUserProfileValidToken() throws UserException {
        String jwt = "valid-jwt-token";
        when(tokenProvider.getEmailFromToken(jwt)).thenReturn("user1@example.com");
        when(userRepository.findByEmail("user1@example.com")).thenReturn(user1);

        User profile = userService.findUserProfile(jwt);

        assertNotNull(profile);
        assertEquals("user1@example.com", profile.getEmail());
        verify(tokenProvider, times(1)).getEmailFromToken(jwt);
        verify(userRepository, times(1)).findByEmail("user1@example.com");
    }

    @Test
    void testFindUserProfileInvalidToken() {
        String jwt = "invalid-jwt-token";
        when(tokenProvider.getEmailFromToken(jwt)).thenReturn(null);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            userService.findUserProfile(jwt);
        });

        assertEquals("Recieved invalid token...", exception.getMessage());
        verify(tokenProvider, times(1)).getEmailFromToken(jwt);
        verify(userRepository, times(0)).findByEmail(anyString());
    }

    @Test
    void testFindUserProfileUserNotFound() {
        String jwt = "valid-jwt-token";
        when(tokenProvider.getEmailFromToken(jwt)).thenReturn("user1@example.com");
        when(userRepository.findByEmail("user1@example.com")).thenReturn(null);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.findUserProfile(jwt);
        });

        assertEquals("User not found with the provided email ", exception.getMessage());
        verify(tokenProvider, times(1)).getEmailFromToken(jwt);
        verify(userRepository, times(1)).findByEmail("user1@example.com");
    }

    @Test
    void testUpdateUserSuccess() throws UserException {
        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("UpdatedName");
        req.setProfile("UpdatedProfile");

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updatedUser = userService.updateUser(1, req);

        assertNotNull(updatedUser);
        assertEquals("UpdatedName", updatedUser.getName());
        assertEquals("UpdatedProfile", updatedUser.getProfile());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testUpdateUserNotFound() {
        UpdateUserRequest req = new UpdateUserRequest();
        req.setName("UpdatedName");

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> {
            userService.updateUser(1, req);
        });

        assertEquals("The requested user is not found", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testSearchUser() {
        String query = "User";
        User user2 = new User();
        user2.setId(2);
        user2.setName("User2");

        when(userRepository.searchUser(query)).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.searchUser(query);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("User1", users.get(0).getName());
        assertEquals("User2", users.get(1).getName());
        verify(userRepository, times(1)).searchUser(query);
    }
}
