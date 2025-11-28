package com.books.observer;

import com.books.entities.Order;
import com.books.entities.OrderStatus;

public class SMSNotificationObserver implements OrderStatusObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        String message = String.format(
                "Статус заказа #%s: %s",
                order.getId(), newStatus
        );

        System.out.println("Отправка SMS пользователю: " + order.getUser().getPhone());
        System.out.println("Сообщение: " + message);
    }
}