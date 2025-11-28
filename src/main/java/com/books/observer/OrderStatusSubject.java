package com.books.observer;

import com.books.entities.Order;
import com.books.entities.OrderStatus;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusSubject {
    private final List<OrderStatusObserver> observers = new ArrayList<>();

    public void addObserver(OrderStatusObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderStatusObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        for (OrderStatusObserver observer : observers) {
            observer.update(order, oldStatus, newStatus);
        }
    }
}