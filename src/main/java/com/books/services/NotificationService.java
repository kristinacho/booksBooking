package com.books.services;
import com.books.config.ApplicationConfig;
import com.books.entities.Order;
import com.books.entities.User;
import com.books.notifications.Notification;
import com.books.notifications.NotificationFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ApplicationConfig config;

    public NotificationService(ApplicationConfig config) {
        this.config = config;
    }

    public void sendOrderCreatedNotification(Order order) {
        User user = order.getUser();
        String message = String.format(
                "Уважаемый(ая) %s! Ваш заказ на книгу '%s' создан. Статус: %s",
                user.getFullName(),
                order.getBookInstance().getBook().getTitle(),
                order.getStatus()
        );

        Notification notification = NotificationFactory.createEmailNotification(
                user.getEmail(),
                "Заказ создан",
                message
        );
        notification.send();
    }

    public void sendOverdueNotification(Order order) {
        User user = order.getUser();
        String message = String.format(
                "Уважаемый(ая) %s! Книга '%s' просрочена. Пожалуйста, верните книгу в библиотеку.",
                user.getFullName(),
                order.getBookInstance().getBook().getTitle()
        );

        Notification emailNotification = NotificationFactory.createEmailNotification(
                user.getEmail(),
                "Просрочка возврата книги",
                message
        );
        emailNotification.send();

        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            Notification smsNotification = NotificationFactory.createSMSNotification(
                    user.getPhone(),
                    "Просрочка возврата книги: " + order.getBookInstance().getBook().getTitle()
            );
            smsNotification.send();
        }
    }

    public void sendBookAvailableNotification(User user, String bookTitle) {
        String message = String.format(
                "Уважаемый(ая) %s! Книга '%s' теперь доступна для заказа.",
                user.getFullName(),
                bookTitle
        );

        Notification notification = NotificationFactory.createNotification(
                "EMAIL", // можно сделать настройку предпочтительного типа уведомлений
                user.getEmail(),
                message
        );
        notification.send();
    }
}