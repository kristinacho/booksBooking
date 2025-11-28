package com.books.template;

import com.books.entities.Order;
import com.books.entities.OrderStatus;
import com.books.entities.BookInstanceStatus;

public class OrderCreationProcessor extends OrderProcessingTemplate {
    @Override
    protected BookInstanceStatus getTargetBookStatus() {
        return BookInstanceStatus.RESERVED;
    }

    @Override
    protected OrderStatus getTargetOrderStatus() {
        return OrderStatus.CREATED;
    }

    @Override
    protected void updateOrderStatus(Order order) {
        order.setStatus(OrderStatus.CREATED);
        System.out.println("Статус заказа изменен на: CREATED");
    }

    @Override
    protected void notifyUser(Order order) {
        System.out.println("Уведомление: Ваш заказ создан и ожидает обработки");
    }
}