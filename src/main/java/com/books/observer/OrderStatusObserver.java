package com.books.observer;

import com.books.entities.Order;
import com.books.entities.OrderStatus;

public interface OrderStatusObserver {
    void update(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}