package com.books.services;

import com.books.config.ApplicationConfig;
import com.books.entities.Book;
import com.books.entities.BookInstance;
import com.books.entities.Order;
import com.books.entities.User;
import com.books.notifications.Notification;
import com.books.notifications.NotificationFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private ApplicationConfig config;

    @InjectMocks
    private NotificationService notificationService;

    private User createTestUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("+1234567890");
        return user;
    }

    private Order createTestOrder() {
        User user = createTestUser();
        Book book = new Book();
        book.setTitle("Test Book");

        BookInstance bookInstance = new BookInstance();
        bookInstance.setBook(book);

        Order order = new Order();
        order.setUser(user);
        order.setBookInstance(bookInstance);
        return order;
    }

    // Тесты для sendOrderCreatedNotification()
    @Test
    void Should_SendEmailNotification_When_OrderCreated() {
        // Arrange
        Order order = createTestOrder();

        try (MockedStatic<NotificationFactory> notificationFactory = mockStatic(NotificationFactory.class)) {
            Notification mockNotification = mock(Notification.class);
            notificationFactory.when(() ->
                            NotificationFactory.createEmailNotification(anyString(), anyString(), anyString()))
                    .thenReturn(mockNotification);

            // Act
            notificationService.sendOrderCreatedNotification(order);

            // Assert
            verify(mockNotification, times(1)).send();
        }
    }

    // Тесты для sendOverdueNotification()
    @Test
    void Should_SendEmailAndSms_When_UserHasPhone() {
        // Arrange
        Order order = createTestOrder();

        try (MockedStatic<NotificationFactory> notificationFactory = mockStatic(NotificationFactory.class)) {
            Notification mockEmailNotification = mock(Notification.class);
            Notification mockSmsNotification = mock(Notification.class);

            notificationFactory.when(() ->
                            NotificationFactory.createEmailNotification(anyString(), anyString(), anyString()))
                    .thenReturn(mockEmailNotification);
            notificationFactory.when(() ->
                            NotificationFactory.createSMSNotification(anyString(), anyString()))
                    .thenReturn(mockSmsNotification);

            // Act
            notificationService.sendOverdueNotification(order);

            // Assert
            verify(mockEmailNotification, times(1)).send();
            verify(mockSmsNotification, times(1)).send();
        }
    }

    @Test
    void Should_SendOnlyEmail_When_UserHasNoPhone() {
        // Arrange
        Order order = createTestOrder();
        order.getUser().setPhone(null); // No phone

        try (MockedStatic<NotificationFactory> notificationFactory = mockStatic(NotificationFactory.class)) {
            Notification mockEmailNotification = mock(Notification.class);

            notificationFactory.when(() ->
                            NotificationFactory.createEmailNotification(anyString(), anyString(), anyString()))
                    .thenReturn(mockEmailNotification);

            // Act
            notificationService.sendOverdueNotification(order);

            // Assert
            verify(mockEmailNotification, times(1)).send();
            // SMS should not be created or sent
            notificationFactory.verify(() ->
                    NotificationFactory.createSMSNotification(anyString(), anyString()), never());
        }
    }

    // Тесты для sendBookAvailableNotification()
    @Test
    void Should_SendNotification_When_BookAvailable() {
        // Arrange
        User user = createTestUser();
        String bookTitle = "Available Book";

        try (MockedStatic<NotificationFactory> notificationFactory = mockStatic(NotificationFactory.class)) {
            Notification mockNotification = mock(Notification.class);
            notificationFactory.when(() ->
                            NotificationFactory.createNotification(anyString(), anyString(), anyString()))
                    .thenReturn(mockNotification);

            // Act
            notificationService.sendBookAvailableNotification(user, bookTitle);

            // Assert
            verify(mockNotification, times(1)).send();
        }
    }
}