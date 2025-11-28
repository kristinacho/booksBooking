package com.books.notifications;

public class EmailNotification implements Notification {
    private final String recipient;
    private final String subject;
    private final String message;

    public EmailNotification(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }

    @Override
    public void send() {
        System.out.println("Отправка email на: " + recipient);
        System.out.println("Тема: " + subject);
        System.out.println("Сообщение: " + message);
    }

    @Override
    public String getType() {
        return "EMAIL";
    }

    @Override
    public String getMessage() {
        return message;
    }
}