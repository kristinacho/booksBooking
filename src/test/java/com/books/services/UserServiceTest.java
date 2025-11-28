package com.books.services;

import com.books.config.ApplicationConfig;
import com.books.dto.UserDTO;
import com.books.dto.CreateUserDTO;
import com.books.dto.UpdateUserDTO;
import com.books.entities.User;
import com.books.entities.UserRole;
import com.books.entities.UserStatus;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationConfig config;

    @InjectMocks
    private UserService userService;

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("+1234567890");
        user.setPassword("password");
        user.setRole(UserRole.READER);
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    @Test
    void Should_ReturnAllUsers_When_UsersExist() {
        // Arrange
        User user1 = createTestUser();
        User user2 = createTestUser();
        user2.setEmail("test2@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserDTO> result = userService.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void Should_ReturnUser_When_UserWithIdExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = createTestUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.findById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void Should_CreateUser_When_EmailIsUnique() {
        // Arrange
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setFullName("New User");
        userDTO.setEmail("new@example.com");
        userDTO.setPhone("+1234567890");
        userDTO.setPassword("password");

        User savedUser = createTestUser();
        savedUser.setEmail("new@example.com");

        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void Should_ValidatePassword_When_CorrectPassword() {
        // Arrange
        User user = createTestUser();
        user.setPassword("correctPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.validatePassword("test@example.com", "correctPassword");

        // Assert
        assertTrue(result);
    }

    @Test
    void Should_FindUsersByRole_When_UsersExist() {
        // Arrange
        User user = createTestUser();
        when(userRepository.findByRole(UserRole.READER)).thenReturn(List.of(user));

        // Act
        List<UserDTO> result = userService.findByRole(UserRole.READER);

        // Assert
        assertEquals(1, result.size());
        assertEquals(UserRole.READER, result.get(0).getRole());
    }
}