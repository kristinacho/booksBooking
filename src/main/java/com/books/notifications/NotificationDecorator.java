package com.books.notifications;

public abstract class NotificationDecorator implements Notification {
    protected Notification decoratedNotification;

    public NotificationDecorator(Notification notification) {
        this.decoratedNotification = notification;
    }

    @Override
    public void send() {
        decoratedNotification.send();
    }

    @Override
    public String getType() {
        return decoratedNotification.getType();
    }

    @Override
    public String getMessage() {
        return decoratedNotification.getMessage();
    }
}