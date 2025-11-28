package com.books.template;

import com.books.entities.Order;
import com.books.entities.OrderStatus;
import com.books.entities.BookInstanceStatus;
import java.time.LocalDateTime;

public class OrderIssueProcessor extends OrderProcessingTemplate {
    @Override
    protected BookInstanceStatus getTargetBookStatus() {
        return BookInstanceStatus.ISSUED;
    }

    @Override
    protected OrderStatus getTargetOrderStatus() {
        return OrderStatus.ISSUED;
    }

    @Override
    protected void updateOrderStatus(Order order) {
        order.setStatus(OrderStatus.ISSUED);
        order.setActualIssueDate(LocalDateTime.now());
        System.out.println("🔄 Статус заказа изменен на: ISSUED");
    }

    @Override
    protected void notifyUser(Order order) {
        String message = String.format(
                "Книга '%s' выдана. Вернуть до: %s",
                order.getBookInstance().getBook().getTitle(),
                order.getExpectedReturnDate()
        );
        System.out.println("📨 Уведомление: " + message);
    }
}