package com.books.notifications;

public class SMSNotification implements Notification {
    private final String phoneNumber;
    private final String message;

    public SMSNotification(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    @Override
    public void send() {
        // Здесь была бы реальная логика отправки SMS
        System.out.println("Отправка SMS на: " + phoneNumber);
        System.out.println("Сообщение: " + message);
    }

    @Override
    public String getType() {
        return "SMS";
    }

    @Override
    public String getMessage() {
        return message;
    }
}