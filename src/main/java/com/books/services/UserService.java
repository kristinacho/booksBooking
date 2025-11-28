package com.books.services;

import com.books.builders.UserBuilder;
import com.books.config.ApplicationConfig;
import com.books.dto.CreateUserDTO;
import com.books.dto.UpdateUserDTO;
import com.books.dto.UserDTO;
import com.books.entities.User;
import com.books.entities.UserRole;
import com.books.entities.UserStatus;
import com.books.exceptions.EntityNotFoundException;
import com.books.notifications.Notification;
import com.books.notifications.NotificationFactory;
import com.books.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationConfig config;

    public UserService(UserRepository userRepository, ApplicationConfig config) {
        this.userRepository = userRepository;
        this.config = config; // Singleton injection
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));
        return convertToDTO(user);
    }

    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с email: " + email + " не найден"));
        return convertToDTO(user);
    }

    public UserDTO createUser(CreateUserDTO userDTO) {
        System.out.println("Create User " + userDTO.getFullName() + " " + userDTO.getEmail() + " " + userDTO.getPhone());

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Пользователь с email: " + userDTO.getEmail() + " уже существует");
        }

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        user.setRole(UserRole.READER);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO createUserWithBuilder(String fullName, String email, String phone, String password) {
        System.out.println("Create User with Builder: " + fullName + " " + email);

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Пользователь с email: " + email + " уже существует");
        }

        // Использование Builder паттерна
        User user = UserBuilder.create()
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setPassword(password)
                .setRole(UserRole.READER)
                .setStatus(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        // Отправка уведомления с помощью Factory Method
        Notification welcomeNotification = NotificationFactory.createEmailNotification(
                email,
                "Добро пожаловать в " + config.getLibraryName() + "!",
                "Уважаемый(ая) " + fullName + "! Вы успешно зарегистрированы в системе."
        );
        welcomeNotification.send();

        return convertToDTO(savedUser);
    }

    // НОВЫЙ МЕТОД для создания библиотекаря с Builder
    public UserDTO createLibrarian(String fullName, String email, String phone, String password) {
        System.out.println("Create Librarian with Builder: " + fullName);

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Пользователь с email: " + email + " уже существует");
        }

        // Builder для создания библиотекаря
        User librarian = UserBuilder.create()
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setPassword(password)
                .setRole(UserRole.LIBRARIAN) // Другая роль
                .setStatus(UserStatus.ACTIVE)
                .build();

        User savedLibrarian = userRepository.save(librarian);

        Notification notification = NotificationFactory.createEmailNotification(
                email,
                "Регистрация библиотекаря",
                "Уважаемый(ая) " + fullName + "! Вы зарегистрированы как библиотекарь."
        );
        notification.send();

        return convertToDTO(savedLibrarian);
    }

    public UserDTO updateUser(UUID id, UpdateUserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));

        if (userDTO.getFullName() != null) {
            user.setFullName(userDTO.getFullName());
        }
        if (userDTO.getEmail() != null) {
            if (!user.getEmail().equals(userDTO.getEmail()) &&
                    userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Пользователь с email: " + userDTO.getEmail() + " уже существует");
            }
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhone() != null) {
            user.setPhone(userDTO.getPhone());
        }
        if (userDTO.getStatus() != null) {
            user.setStatus(userDTO.getStatus());
        }

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO updateUserRole(UUID id, UserRole role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));

        user.setRole(role);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));
        userRepository.delete(user);
    }

    public void resetPassword(UUID id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + id + " не найден"));

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public List<UserDTO> findByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> findByStatus(UserStatus status) {
        return userRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean validatePassword(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.getRole()
        );
    }
}