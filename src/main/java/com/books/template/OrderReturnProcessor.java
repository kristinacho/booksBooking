package com.books.template;

import com.books.entities.Order;
import com.books.entities.OrderStatus;
import com.books.entities.BookInstanceStatus;
import com.books.strategy.FineCalculationStrategy;
import java.time.LocalDateTime;

public class OrderReturnProcessor extends OrderProcessingTemplate {
    private final FineCalculationStrategy fineStrategy;

    public OrderReturnProcessor(FineCalculationStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }

    @Override
    protected BookInstanceStatus getTargetBookStatus() {
        return BookInstanceStatus.AVAILABLE;
    }

    @Override
    protected OrderStatus getTargetOrderStatus() {
        return OrderStatus.RETURNED;
    }

    @Override
    protected void updateOrderStatus(Order order) {
        order.setStatus(OrderStatus.RETURNED);
        order.setActualReturnDate(LocalDateTime.now());

        double fine = fineStrategy.calculateFine(
                order.getExpectedReturnDate(),
                order.getActualReturnDate(),
                50.0
        );

        if (fine > 0) {
            System.out.println("Рассчитан штраф: " + fine + " руб.");
        }

        System.out.println("Статус заказа изменен на: RETURNED");
    }

    @Override
    protected void notifyUser(Order order) {
        System.out.println("Уведомление: Книга возвращена в библиотеку");
    }
}