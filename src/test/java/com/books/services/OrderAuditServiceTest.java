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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderAuditServiceTest {

    @Mock
    private OrderAuditRepository orderAuditRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderAuditService orderAuditService;

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("Test User");
        return user;
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(createTestUser());
        return order;
    }

    private OrderAudit createTestOrderAudit() {
        OrderAudit audit = new OrderAudit();
        audit.setId(UUID.randomUUID());
        audit.setUser(createTestUser());
        audit.setOrder(createTestOrder());
        audit.setOperation(AuditOperation.ORDER);
        audit.setOldValue("Old Status");
        audit.setNewValue("New Status");
        audit.setCreatedAt(LocalDateTime.now());
        audit.setUpdatedAt(LocalDateTime.now());
        return audit;
    }

    // Тесты для findAll()
    @Test
    void Should_ReturnAllOrderAudits_When_AuditsExist() {
        // Arrange
        OrderAudit audit = createTestOrderAudit();
        when(orderAuditRepository.findAll()).thenReturn(List.of(audit));

        // Act
        List<OrderAuditDTO> result = orderAuditService.findAll();

        // Assert
        assertEquals(1, result.size());
        verify(orderAuditRepository, times(1)).findAll();
    }

    @Test
    void Should_ReturnEmptyList_When_NoOrderAuditsExist() {
        // Arrange
        when(orderAuditRepository.findAll()).thenReturn(List.of());

        // Act
        List<OrderAuditDTO> result = orderAuditService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(orderAuditRepository, times(1)).findAll();
    }

    // Тесты для findById()
    @Test
    void Should_ReturnOrderAudit_When_AuditWithIdExists() {
        // Arrange
        UUID auditId = UUID.randomUUID();
        OrderAudit audit = createTestOrderAudit();
        audit.setId(auditId);
        when(orderAuditRepository.findById(auditId)).thenReturn(Optional.of(audit));

        // Act
        OrderAuditDTO result = orderAuditService.findById(auditId);

        // Assert
        assertNotNull(result);
        assertEquals(auditId, result.getId());
        verify(orderAuditRepository, times(1)).findById(auditId);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_AuditWithIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(orderAuditRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderAuditService.findById(nonExistentId);
        });
        verify(orderAuditRepository, times(1)).findById(nonExistentId);
    }

    // Тесты для findByOrderId()
    @Test
    void Should_ReturnOrderAudits_When_OrderExistsAndHasAudits() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = createTestOrder();
        order.setId(orderId);
        OrderAudit audit = createTestOrderAudit();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderAuditRepository.findByOrder(order)).thenReturn(List.of(audit));

        // Act
        List<OrderAuditDTO> result = orderAuditService.findByOrderId(orderId);

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderAuditRepository, times(1)).findByOrder(order);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_OrderNotFound() {
        // Arrange
        UUID nonExistentOrderId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderAuditService.findByOrderId(nonExistentOrderId);
        });
        verify(orderRepository, times(1)).findById(nonExistentOrderId);
        verify(orderAuditRepository, never()).findByOrder(any(Order.class));
    }

    // Тесты для findByUserId()
    @Test
    void Should_ReturnOrderAudits_When_UserExistsAndHasAudits() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = createTestUser();
        user.setId(userId);
        OrderAudit audit = createTestOrderAudit();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderAuditRepository.findByUser(user)).thenReturn(List.of(audit));

        // Act
        List<OrderAuditDTO> result = orderAuditService.findByUserId(userId);

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(userId);
        verify(orderAuditRepository, times(1)).findByUser(user);
    }

    // Тесты для findByOperation()
    @Test
    void Should_ReturnOrderAudits_When_AuditsWithOperationExist() {
        // Arrange
        OrderAudit audit = createTestOrderAudit();
        when(orderAuditRepository.findByOperation(AuditOperation.ORDER)).thenReturn(List.of(audit));

        // Act
        List<OrderAuditDTO> result = orderAuditService.findByOperation(AuditOperation.ORDER);

        // Assert
        assertEquals(1, result.size());
        assertEquals(AuditOperation.ORDER, result.get(0).getOperation());
        verify(orderAuditRepository, times(1)).findByOperation(AuditOperation.ORDER);
    }

    // Тесты для findByOrderAndOperation()
    @Test
    void Should_ReturnOrderAudits_When_OrderExistsAndHasMatchingOperation() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = createTestOrder();
        order.setId(orderId);
        OrderAudit audit = createTestOrderAudit();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderAuditRepository.findByOrderAndOperation(order, AuditOperation.ORDER))
                .thenReturn(List.of(audit));

        // Act
        List<OrderAuditDTO> result = orderAuditService.findByOrderAndOperation(orderId, AuditOperation.ORDER);

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderAuditRepository, times(1)).findByOrderAndOperation(order, AuditOperation.ORDER);
    }
}