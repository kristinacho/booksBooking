package com.books.observer;

import com.books.entities.Order;
import com.books.entities.OrderStatus;

public class AuditLogObserver implements OrderStatusObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("Аудит: Заказ #" + order.getId() +
                " | Пользователь: " + order.getUser().getFullName() +
                " | Изменение статуса: " + oldStatus + " -> " + newStatus);
    }
}