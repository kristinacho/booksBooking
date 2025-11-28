package com.books.notifications;

public interface Notification {
    void send();
    String getType();
    String getMessage();
}