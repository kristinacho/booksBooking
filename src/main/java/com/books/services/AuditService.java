package com.books.services;

import com.books.entities.AuditOperation;
import com.books.entities.Order;
import com.books.entities.OrderAudit;
import com.books.entities.User;
import com.books.repositories.OrderAuditRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final OrderAuditRepository orderAuditRepository;

    public AuditService(OrderAuditRepository orderAuditRepository) {
        this.orderAuditRepository = orderAuditRepository;
    }

    public void logOrderOperation(User user, Order order, AuditOperation operation, String oldValue, String newValue) {
        OrderAudit audit = new OrderAudit();
        audit.setUser(user);
        audit.setOrder(order);
        audit.setOperation(operation);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);

        orderAuditRepository.save(audit);
    }
}