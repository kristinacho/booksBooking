package com.books.repositories;

import com.books.entities.OrderAudit;
import com.books.entities.AuditOperation;
import com.books.entities.Order;
import com.books.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderAuditRepository extends JpaRepository<OrderAudit, UUID> {

    // Поиск записей аудита по заказу
    List<OrderAudit> findByOrder(Order order);

    // Поиск записей аудита по пользователю
    List<OrderAudit> findByUser(User user);

    // Поиск записей аудита по операции
    List<OrderAudit> findByOperation(AuditOperation operation);

    // Поиск записей аудита по заказу и операции
    List<OrderAudit> findByOrderAndOperation(Order order, AuditOperation operation);
}