package com.books.observer;

import com.books.entities.Order;
import com.books.entities.OrderStatus;

public class EmailNotificationObserver implements OrderStatusObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String message = String.format(
                "Статус вашего заказа #%s изменен: %s -> %s",
                order.getId(), oldStatus, newStatus
        );

        System.out.println("📧 Отправка email пользователю: " + order.getUser().getEmail());
        System.out.println("📧 Сообщение: " + message);
    }
}