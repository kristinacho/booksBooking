package com.books.notifications;

public class NotificationFactory {

    public static Notification createEmailNotification(String recipient, String subject, String message) {
        return new EmailNotification(recipient, subject, message);
    }

    public static Notification createSMSNotification(String phoneNumber, String message) {
        return new SMSNotification(phoneNumber, message);
    }

    public static Notification createNotification(String type, String recipient, String message) {
        switch (type.toUpperCase()) {
            case "EMAIL":
                return createEmailNotification(recipient, "Уведомление от библиотеки", message);
            case "SMS":
                return createSMSNotification(recipient, message);
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }
}