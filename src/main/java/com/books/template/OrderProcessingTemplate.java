package com.books.template;

import com.books.entities.Order;
import com.books.entities.OrderStatus;
import com.books.entities.BookInstance;
import com.books.entities.BookInstanceStatus;

public abstract class OrderProcessingTemplate {

    public final void processOrder(Order order) {
        validateOrder(order);
        updateBookInstanceStatus(order);
        updateOrderStatus(order);
        notifyUser(order);
        logProcessing(order);
    }

    protected void validateOrder(Order order) {
        if (order.getUser() == null) {
            throw new IllegalArgumentException("Заказ должен иметь пользователя");
        }
        if (order.getBookInstance() == null) {
            throw new IllegalArgumentException("Заказ должен иметь экземпляр книги");
        }
        System.out.println("✅ Валидация заказа пройдена");
    }

    protected void updateBookInstanceStatus(Order order) {
        BookInstance bookInstance = order.getBookInstance();
        bookInstance.setStatus(getTargetBookStatus());
        System.out.println("🔄 Статус экземпляра книги изменен на: " + getTargetBookStatus());
    }

    protected void logProcessing(Order order) {
        System.out.println("📝 Лог: Обработка заказа #" + order.getId() +
                " завершена. Новый статус: " + getTargetOrderStatus());
    }

    protected abstract BookInstanceStatus getTargetBookStatus();
    protected abstract OrderStatus getTargetOrderStatus();
    protected abstract void updateOrderStatus(Order order);
    protected abstract void notifyUser(Order order);
}