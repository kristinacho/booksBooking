package com.books.services;

import com.books.dto.OrderAuditDTO;
import com.books.entities.AuditOperation;
import com.books.entities.Order;
import com.books.entities.OrderAudit;
import com.books.entities.User;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.OrderAuditRepository;
import com.books.repositories.OrderRepository;
import com.books.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderAuditService {

    private final OrderAuditRepository orderAuditRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderAuditService(OrderAuditRepository orderAuditRepository,
                             OrderRepository orderRepository,
                             UserRepository userRepository) {
        this.orderAuditRepository = orderAuditRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<OrderAuditDTO> findAll() {
        return orderAuditRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderAuditDTO findById(UUID id) {
        OrderAudit orderAudit = orderAuditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Запись аудита с id: " + id + " не найдена"));
        return convertToDTO(orderAudit);
    }

    public List<OrderAuditDTO> findByOrderId(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id: " + orderId + " не найден"));
        return orderAuditRepository.findByOrder(order).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderAuditDTO> findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));
        return orderAuditRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderAuditDTO> findByOperation(AuditOperation operation) {
        return orderAuditRepository.findByOperation(operation).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderAuditDTO> findByOrderAndOperation(UUID orderId, AuditOperation operation) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id: " + orderId + " не найден"));
        return orderAuditRepository.findByOrderAndOperation(order, operation).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderAuditDTO convertToDTO(OrderAudit orderAudit) {
        return new OrderAuditDTO(
                orderAudit.getId(),
                orderAudit.getCreatedAt(),
                orderAudit.getUpdatedAt(),
                orderAudit.getUser().getId(),
                orderAudit.getUser().getFullName(),
                orderAudit.getOperation(),
                orderAudit.getOldValue(),
                orderAudit.getNewValue(),
                orderAudit.getOrder().getId()
        );
    }
}