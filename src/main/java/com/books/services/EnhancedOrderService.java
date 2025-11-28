package com.books.services;

import com.books.strategy.*;
import com.books.observer.*;
import com.books.template.*;
import com.books.entities.Order;
import com.books.entities.OrderStatus;
import com.books.dto.UpdateOrderDTO;
import org.springframework.stereotype.Service;

@Service
public class EnhancedOrderService {
    private final OrderService orderService;
    private final OrderStatusSubject statusSubject;
    private final FineCalculationStrategy fineStrategy;

    public EnhancedOrderService(OrderService orderService) {
        this.orderService = orderService;
        this.statusSubject = new OrderStatusSubject();
        this.fineStrategy = new ProgressiveFineStrategy();

        statusSubject.addObserver(new EmailNotificationObserver());
        statusSubject.addObserver(new SMSNotificationObserver());
        statusSubject.addObserver(new AuditLogObserver());
    }

    public void processOrderCreation(Order order) {
        OrderCreationProcessor processor = new OrderCreationProcessor();
        processor.processOrder(order);
        orderService.updateOrder(order.getId(), convertToUpdateDTO(order));
    }

    public void processOrderIssue(Order order) {
        OrderIssueProcessor processor = new OrderIssueProcessor();
        processor.processOrder(order);

        statusSubject.notifyObservers(order, OrderStatus.CREATED, OrderStatus.ISSUED);
        orderService.updateOrder(order.getId(), convertToUpdateDTO(order));
    }

    public void processOrderReturn(Order order) {
        OrderReturnProcessor processor = new OrderReturnProcessor(fineStrategy);
        processor.processOrder(order);

        statusSubject.notifyObservers(order, OrderStatus.ISSUED, OrderStatus.RETURNED);
        orderService.updateOrder(order.getId(), convertToUpdateDTO(order));
    }

    public double calculateFine(Order order) {
        return fineStrategy.calculateFine(
                order.getExpectedReturnDate(),
                order.getActualReturnDate() != null ? order.getActualReturnDate() : java.time.LocalDateTime.now(),
                50.0
        );
    }

    private UpdateOrderDTO convertToUpdateDTO(Order order) {
        UpdateOrderDTO dto = new UpdateOrderDTO();
        dto.setStatus(order.getStatus());
        dto.setActualIssueDate(order.getActualIssueDate());
        dto.setActualReturnDate(order.getActualReturnDate());
        return dto;
    }
}