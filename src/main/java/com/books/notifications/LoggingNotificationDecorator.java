package com.books.notifications;

public class LoggingNotificationDecorator extends NotificationDecorator {
    public LoggingNotificationDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Логирование: Отправка " + getType() + " уведомления");
        System.out.println("Сообщение: " + getMessage());
        System.out.println("Время: " + java.time.LocalDateTime.now());
        super.send();
        System.out.println("Уведомление отправлено успешно");
    }
}