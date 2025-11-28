package com.books.controllers;

import com.books.dto.CreateOrderDTO;
import com.books.dto.OrderDTO;
import com.books.dto.UpdateOrderDTO;
import com.books.entities.OrderStatus;
import com.books.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private UUID orderId;
    private UUID userId;
    private UUID bookInstanceId;
    private OrderDTO sampleOrder;
    private CreateOrderDTO createOrderDTO;
    private UpdateOrderDTO updateOrderDTO;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        userId = UUID.randomUUID();
        bookInstanceId = UUID.randomUUID();
        sampleOrder = createSampleOrderDTO(orderId, userId, bookInstanceId);
        createOrderDTO = createSampleCreateOrderDTO(userId, bookInstanceId);
        updateOrderDTO = createSampleUpdateOrderDTO();
    }

    private OrderDTO createSampleOrderDTO(UUID orderId, UUID userId, UUID bookInstanceId) {
        OrderDTO order = new OrderDTO();
        order.setId(orderId);
        order.setUserId(userId);
        order.setUserFullName("John Doe");
        order.setBookInstanceId(bookInstanceId);
        order.setBookTitle("Test Book");
        order.setBookAuthor("Test Author");
        order.setLibraryId(UUID.randomUUID());
        order.setLibraryName("Main Library");
        order.setCreatedAt(LocalDateTime.now());
        order.setReservationDeadline(LocalDateTime.now().plusDays(7));
        order.setExpectedReturnDate(LocalDateTime.now().plusDays(14));
        order.setStatus(OrderStatus.CREATED);
        return order;
    }

    private CreateOrderDTO createSampleCreateOrderDTO(UUID userId, UUID bookInstanceId) {
        CreateOrderDTO order = new CreateOrderDTO();
        order.setUserId(userId);
        order.setBookInstanceId(bookInstanceId);
        order.setReservationDeadline(LocalDateTime.now().plusDays(7));
        order.setExpectedReturnDate(LocalDateTime.now().plusDays(14));
        return order;
    }

    private UpdateOrderDTO createSampleUpdateOrderDTO() {
        UpdateOrderDTO order = new UpdateOrderDTO();
        order.setStatus(OrderStatus.READY_FOR_ISSUE);
        order.setReservationDeadline(LocalDateTime.now().plusDays(5));
        order.setExpectedReturnDate(LocalDateTime.now().plusDays(12));
        order.setActualIssueDate(LocalDateTime.now());
        return order;
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Given
        List<OrderDTO> expectedOrders = Arrays.asList(
                sampleOrder,
                createSampleOrderDTO(UUID.randomUUID(), userId, bookInstanceId)
        );
        when(orderService.findAll()).thenReturn(expectedOrders);

        // When
        ResponseEntity<List<OrderDTO>> response = orderController.getAllOrders();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrders, response.getBody());
        verify(orderService).findAll();
    }

    @Test
    void getOrderById_WithValidId_ShouldReturnOrder() {
        // Given
        when(orderService.findById(orderId)).thenReturn(sampleOrder);

        // When
        ResponseEntity<OrderDTO> response = orderController.getOrderById(orderId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleOrder, response.getBody());
        verify(orderService).findById(orderId);
    }

    @Test
    void getOrdersByUser_WithValidUserId_ShouldReturnUserOrders() {
        // Given
        List<OrderDTO> expectedOrders = Collections.singletonList(sampleOrder);
        when(orderService.findByUserId(userId)).thenReturn(expectedOrders);

        // When
        ResponseEntity<List<OrderDTO>> response = orderController.getOrdersByUser(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrders, response.getBody());
        verify(orderService).findByUserId(userId);
    }

    @Test
    void getActiveOrdersByUser_WithValidUserId_ShouldReturnActiveOrders() {
        // Given
        List<OrderDTO> expectedOrders = Collections.singletonList(sampleOrder);
        when(orderService.findActiveByUserId(userId)).thenReturn(expectedOrders);

        // When
        ResponseEntity<List<OrderDTO>> response = orderController.getActiveOrdersByUser(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrders, response.getBody());
        verify(orderService).findActiveByUserId(userId);
    }

    @Test
    void getOrdersByStatus_WithCreatedStatus_ShouldReturnFilteredOrders() {
        // Given
        OrderStatus status = OrderStatus.CREATED;
        List<OrderDTO> expectedOrders = Collections.singletonList(sampleOrder);
        when(orderService.findByStatus(status)).thenReturn(expectedOrders);

        // When
        ResponseEntity<List<OrderDTO>> response = orderController.getOrdersByStatus(status);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedOrders, response.getBody());
        verify(orderService).findByStatus(status);
    }

    @Test
    void createOrder_WithValidData_ShouldReturnCreatedOrder() {
        // Given
        when(orderService.createOrder(any(CreateOrderDTO.class))).thenReturn(sampleOrder);

        // When
        ResponseEntity<OrderDTO> response = orderController.createOrder(createOrderDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleOrder, response.getBody());
        verify(orderService).createOrder(any(CreateOrderDTO.class));
    }

    @Test
    void updateOrder_WithValidData_ShouldReturnUpdatedOrder() {
        // Given
        OrderDTO updatedOrder = createSampleOrderDTO(orderId, userId, bookInstanceId);
        updatedOrder.setStatus(OrderStatus.READY_FOR_ISSUE);
        when(orderService.updateOrder(eq(orderId), any(UpdateOrderDTO.class))).thenReturn(updatedOrder);

        // When
        ResponseEntity<OrderDTO> response = orderController.updateOrder(orderId, updateOrderDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrder, response.getBody());
        verify(orderService).updateOrder(eq(orderId), any(UpdateOrderDTO.class));
    }

    @Test
    void updateOrderStatus_WithReadyForIssueStatus_ShouldReturnUpdatedOrder() {
        // Given
        OrderStatus newStatus = OrderStatus.READY_FOR_ISSUE;
        OrderDTO updatedOrder = createSampleOrderDTO(orderId, userId, bookInstanceId);
        updatedOrder.setStatus(newStatus);
        when(orderService.updateOrderStatus(orderId, newStatus)).thenReturn(updatedOrder);

        // When
        ResponseEntity<OrderDTO> response = orderController.updateOrderStatus(orderId, newStatus);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrder, response.getBody());
        verify(orderService).updateOrderStatus(orderId, newStatus);
    }

    @Test
    void cancelOrder_WithValidId_ShouldCallCancelService() {
        // When
        ResponseEntity<Void> response = orderController.cancelOrder(orderId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderService).cancelOrder(orderId);
    }

    @Test
    void deleteOrder_WithValidId_ShouldCallDeleteService() {
        // When
        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderService).deleteOrder(orderId);
    }
}