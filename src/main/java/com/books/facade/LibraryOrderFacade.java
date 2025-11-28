package com.books.facade;

import com.books.notifications.*;
import com.books.external.ExternalEmailService;
import com.books.external.ExternalSMSService;
import com.books.services.OrderService;
import com.books.dto.CreateOrderDTO;
import com.books.dto.OrderDTO;
import com.books.entities.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class LibraryOrderFacade {
    private final OrderService orderService;
    private final ExternalEmailService emailService;
    private final ExternalSMSService smsService;

    public LibraryOrderFacade(OrderService orderService,
                              ExternalEmailService emailService,
                              ExternalSMSService smsService) {
        this.orderService = orderService;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public OrderDTO createOrderWithNotifications(CreateOrderDTO orderDTO,
                                                 String notificationType,
                                                 boolean enableLogging,
                                                 boolean enableCaching) {
        System.out.println("Создание заказа с уведомлениями");

        OrderDTO order = orderService.createOrderWithBuilder(orderDTO);

        String message = String.format(
                "Уважаемый читатель! Ваш заказ на книгу создан. Номер заказа: %s. Статус: %s",
                order.getId(), order.getStatus()
        );

        Notification notification = createNotification(notificationType, orderDTO.getUserId().toString(), message);

        if (enableLogging) {
            notification = new LoggingNotificationDecorator(notification);
        }

        if (enableCaching) {
            notification = new CachingNotificationDecorator(notification);
        }

        notification = new RetryNotificationDecorator(notification, 3);
        notification.send();

        return order;
    }

    public OrderDTO updateOrderStatusWithNotification(UUID orderId, OrderStatus newStatus,
                                                      String notificationType) {
        System.out.println("Обновление статуса заказа");

        OrderDTO order = orderService.updateOrderStatus(orderId, newStatus);

        String message = String.format(
                "Статус вашего заказа %s изменен на: %s",
                orderId, newStatus
        );

        Notification notification = createNotification(notificationType, order.getUserId().toString(), message);
        notification = new LoggingNotificationDecorator(notification);
        notification.send();

        return order;
    }

    public OrderSummary getOrderSummary(UUID orderId) {
        System.out.println("Получение полной информации о заказе");

        OrderDTO order = orderService.findById(orderId);

        return new OrderSummary(
                order.getId(),
                order.getUserFullName(),
                order.getBookTitle(),
                order.getBookAuthor(),
                order.getLibraryName(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getExpectedReturnDate(),
                order.getActualReturnDate()
        );
    }

    private Notification createNotification(String type, String recipient, String message) {
        switch (type.toUpperCase()) {
            case "EMAIL":
                return new EmailServiceAdapter(emailService, recipient, "Уведомление библиотеки", message);
            case "SMS":
                return new SMSServiceAdapter(smsService, recipient, message);
            case "INTERNAL_EMAIL":
                return NotificationFactory.createEmailNotification(recipient, "Уведомление библиотеки", message);
            case "INTERNAL_SMS":
                return NotificationFactory.createSMSNotification(recipient, message);
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }

    public static class OrderSummary {
        private final UUID orderId;
        private final String userFullName;
        private final String bookTitle;
        private final String bookAuthor;
        private final String libraryName;
        private final OrderStatus status;
        private final LocalDateTime createdAt;
        private final LocalDateTime expectedReturnDate;
        private final LocalDateTime actualReturnDate;

        public OrderSummary(UUID orderId, String userFullName, String bookTitle,
                            String bookAuthor, String libraryName, OrderStatus status,
                            LocalDateTime createdAt, LocalDateTime expectedReturnDate,
                            LocalDateTime actualReturnDate) {
            this.orderId = orderId;
            this.userFullName = userFullName;
            this.bookTitle = bookTitle;
            this.bookAuthor = bookAuthor;
            this.libraryName = libraryName;
            this.status = status;
            this.createdAt = createdAt;
            this.expectedReturnDate = expectedReturnDate;
            this.actualReturnDate = actualReturnDate;
        }

        public UUID getOrderId() { return orderId; }
        public String getUserFullName() { return userFullName; }
        public String getBookTitle() { return bookTitle; }
        public String getBookAuthor() { return bookAuthor; }
        public String getLibraryName() { return libraryName; }
        public OrderStatus getStatus() { return status; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getExpectedReturnDate() { return expectedReturnDate; }
        public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    }
}