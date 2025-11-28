package com.books.services;

import com.books.builders.OrderBuilder;
import com.books.config.ApplicationConfig;
import com.books.dto.CreateOrderDTO;
import com.books.dto.OrderDTO;
import com.books.dto.UpdateOrderDTO;
import com.books.entities.*;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.BookInstanceRepository;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookInstanceRepository bookInstanceRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private ApplicationConfig config;

    @InjectMocks
    private OrderService orderService;

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        return user;
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        return book;
    }

    private Library createTestLibrary() {
        Library library = new Library();
        library.setId(UUID.randomUUID());
        library.setName("Test Library");
        return library;
    }

    private BookInstance createTestBookInstance() {
        BookInstance bookInstance = new BookInstance();
        bookInstance.setId(UUID.randomUUID());
        bookInstance.setBook(createTestBook());
        bookInstance.setLibrary(createTestLibrary());
        bookInstance.setStatus(BookInstanceStatus.AVAILABLE);
        return bookInstance;
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setUser(createTestUser());
        order.setBookInstance(createTestBookInstance());
        order.setStatus(OrderStatus.CREATED);
        order.setReservationDeadline(LocalDateTime.now().plusDays(7));
        order.setExpectedReturnDate(LocalDateTime.now().plusDays(14));
        return order;
    }

    private CreateOrderDTO createTestCreateOrderDTO() {
        CreateOrderDTO dto = new CreateOrderDTO();
        dto.setUserId(UUID.randomUUID());
        dto.setBookInstanceId(UUID.randomUUID());
        dto.setReservationDeadline(LocalDateTime.now().plusDays(7));
        dto.setExpectedReturnDate(LocalDateTime.now().plusDays(14));
        return dto;
    }

    // Тесты для findAll()
    @Test
    void Should_ReturnAllOrders_When_OrdersExist() {
        // Arrange
        Order order = createTestOrder();
        when(orderRepository.findAll()).thenReturn(List.of(order));

        // Act
        List<OrderDTO> result = orderService.findAll();

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void Should_ReturnEmptyList_When_NoOrdersExist() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of());

        // Act
        List<OrderDTO> result = orderService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    // Тесты для findById()
    @Test
    void Should_ReturnOrder_When_OrderWithIdExists() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = createTestOrder();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        OrderDTO result = orderService.findById(orderId);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_OrderWithIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.findById(nonExistentId);
        });
        verify(orderRepository, times(1)).findById(nonExistentId);
    }

    // Тесты для findByUserId()
    @Test
    void Should_ReturnOrders_When_UserExistsAndHasOrders() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = createTestUser();
        user.setId(userId);
        Order order = createTestOrder();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));

        // Act
        List<OrderDTO> result = orderService.findByUserId(userId);

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_UserNotFound() {
        // Arrange
        UUID nonExistentUserId = UUID.randomUUID();
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.findByUserId(nonExistentUserId);
        });
        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(orderRepository, never()).findByUser(any(User.class));
    }

    // Тесты для findByStatus()
    @Test
    void Should_ReturnOrders_When_OrdersWithStatusExist() {
        // Arrange
        Order order = createTestOrder();
        when(orderRepository.findByStatus(OrderStatus.CREATED)).thenReturn(List.of(order));

        // Act
        List<OrderDTO> result = orderService.findByStatus(OrderStatus.CREATED);

        // Assert
        assertEquals(1, result.size());
        assertEquals(OrderStatus.CREATED, result.get(0).getStatus());
        verify(orderRepository, times(1)).findByStatus(OrderStatus.CREATED);
    }

    // Тесты для findActiveByUserId()
    @Test
    void Should_ReturnActiveOrders_When_UserHasActiveOrders() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = createTestUser();
        user.setId(userId);
        Order activeOrder = createTestOrder();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findByUserAndStatusIn(any(User.class), any(List.class)))
                .thenReturn(List.of(activeOrder));

        // Act
        List<OrderDTO> result = orderService.findActiveByUserId(userId);

        // Assert
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(userId);
        verify(orderRepository, times(1)).findByUserAndStatusIn(any(User.class), any(List.class));
    }

    // Тесты для createOrder()
    @Test
    void Should_CreateOrder_When_ValidDataAndBookAvailable() {
        // Arrange
        CreateOrderDTO createDTO = createTestCreateOrderDTO();
        User user = createTestUser();
        user.setId(createDTO.getUserId());
        BookInstance bookInstance = createTestBookInstance();
        bookInstance.setId(createDTO.getBookInstanceId());
        Order savedOrder = createTestOrder();

        when(userRepository.findById(createDTO.getUserId())).thenReturn(Optional.of(user));
        when(bookInstanceRepository.findById(createDTO.getBookInstanceId())).thenReturn(Optional.of(bookInstance));
        when(bookInstanceRepository.save(any(BookInstance.class))).thenReturn(bookInstance);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        OrderDTO result = orderService.createOrder(createDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(createDTO.getUserId());
        verify(bookInstanceRepository, times(1)).findById(createDTO.getBookInstanceId());
        verify(bookInstanceRepository, times(1)).save(bookInstance);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(auditService, times(1)).logOrderOperation(any(User.class), any(Order.class), any(AuditOperation.class), any(), any());
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_UserNotFoundDuringCreation() {
        // Arrange
        CreateOrderDTO createDTO = createTestCreateOrderDTO();
        when(userRepository.findById(createDTO.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(createDTO);
        });
        verify(userRepository, times(1)).findById(createDTO.getUserId());
        verify(bookInstanceRepository, never()).findById(any(UUID.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void Should_ThrowRuntimeException_When_BookInstanceNotAvailable() {
        // Arrange
        CreateOrderDTO createDTO = createTestCreateOrderDTO();
        User user = createTestUser();
        user.setId(createDTO.getUserId());
        BookInstance bookInstance = createTestBookInstance();
        bookInstance.setId(createDTO.getBookInstanceId());
        bookInstance.setStatus(BookInstanceStatus.RESERVED); // Not available

        when(userRepository.findById(createDTO.getUserId())).thenReturn(Optional.of(user));
        when(bookInstanceRepository.findById(createDTO.getBookInstanceId())).thenReturn(Optional.of(bookInstance));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(createDTO);
        });

        assertEquals("Экземпляр книги недоступен для заказа", exception.getMessage());
        verify(userRepository, times(1)).findById(createDTO.getUserId());
        verify(bookInstanceRepository, times(1)).findById(createDTO.getBookInstanceId());
        verify(bookInstanceRepository, never()).save(any(BookInstance.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    // Тесты для createOrderWithBuilder()
    @Test
    void Should_CreateOrderWithBuilder_When_WithinOrderLimit() {
        // Arrange
        CreateOrderDTO createDTO = createTestCreateOrderDTO();
        User user = createTestUser();
        user.setId(createDTO.getUserId());
        BookInstance bookInstance = createTestBookInstance();
        bookInstance.setId(createDTO.getBookInstanceId());
        Order savedOrder = createTestOrder();

        when(userRepository.findById(createDTO.getUserId())).thenReturn(Optional.of(user));
        when(bookInstanceRepository.findById(createDTO.getBookInstanceId())).thenReturn(Optional.of(bookInstance));
        when(orderRepository.findByUserAndStatusIn(any(User.class), any(List.class))).thenReturn(List.of());
        when(config.getMaxActiveOrdersPerUser()).thenReturn(5);
        when(config.getReservationPeriodDays()).thenReturn(7);
        when(bookInstanceRepository.save(any(BookInstance.class))).thenReturn(bookInstance);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        OrderDTO result = orderService.createOrderWithBuilder(createDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(createDTO.getUserId());
        verify(bookInstanceRepository, times(1)).findById(createDTO.getBookInstanceId());
        verify(orderRepository, times(1)).findByUserAndStatusIn(any(User.class), any(List.class));
        verify(bookInstanceRepository, times(1)).save(bookInstance);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(auditService, times(1)).logOrderOperation(any(User.class), any(Order.class), any(AuditOperation.class), any(), any());
    }

    @Test
    void Should_ThrowRuntimeException_When_ExceedsMaxActiveOrders() {
        // Arrange
        CreateOrderDTO createDTO = createTestCreateOrderDTO();
        User user = createTestUser();
        user.setId(createDTO.getUserId());
        BookInstance bookInstance = createTestBookInstance();
        bookInstance.setId(createDTO.getBookInstanceId());
        List<Order> activeOrders = List.of(createTestOrder(), createTestOrder(), createTestOrder()); // 3 active orders

        when(userRepository.findById(createDTO.getUserId())).thenReturn(Optional.of(user));
        when(bookInstanceRepository.findById(createDTO.getBookInstanceId())).thenReturn(Optional.of(bookInstance));
        when(orderRepository.findByUserAndStatusIn(any(User.class), any(List.class))).thenReturn(activeOrders);
        when(config.getMaxActiveOrdersPerUser()).thenReturn(2); // Limit is 2

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrderWithBuilder(createDTO);
        });

        assertTrue(exception.getMessage().contains("Превышено максимальное количество активных заказов"));
        verify(userRepository, times(1)).findById(createDTO.getUserId());
        verify(bookInstanceRepository, times(1)).findById(createDTO.getBookInstanceId());
        verify(orderRepository, times(1)).findByUserAndStatusIn(any(User.class), any(List.class));
        verify(bookInstanceRepository, never()).save(any(BookInstance.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    // Тесты для updateOrder()
    @Test
    void Should_UpdateOrder_When_ValidDataProvided() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createTestOrder();
        existingOrder.setId(orderId);

        UpdateOrderDTO updateDTO = new UpdateOrderDTO();
        updateDTO.setReservationDeadline(LocalDateTime.now().plusDays(10));
        updateDTO.setExpectedReturnDate(LocalDateTime.now().plusDays(20));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        // Act
        OrderDTO result = orderService.updateOrder(orderId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void Should_UpdateBookInstanceStatus_When_OrderReturned() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createTestOrder();
        existingOrder.setId(orderId);
        BookInstance bookInstance = existingOrder.getBookInstance();

        UpdateOrderDTO updateDTO = new UpdateOrderDTO();
        updateDTO.setStatus(OrderStatus.RETURNED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(bookInstanceRepository.save(any(BookInstance.class))).thenReturn(bookInstance);
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        // Act
        OrderDTO result = orderService.updateOrder(orderId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(bookInstanceRepository, times(1)).save(bookInstance);
        verify(orderRepository, times(1)).save(existingOrder);
        verify(auditService, times(1)).logOrderOperation(any(User.class), any(Order.class), any(AuditOperation.class), any(), any());
    }

    // Тесты для updateOrderStatus()
    @Test
    void Should_UpdateOrderStatus_When_ValidStatusProvided() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createTestOrder();
        existingOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        // Act
        OrderDTO result = orderService.updateOrderStatus(orderId, OrderStatus.READY_FOR_ISSUE);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(existingOrder);
    }

    // Тесты для cancelOrder()
    @Test
    void Should_CancelOrder_When_OrderExists() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order existingOrder = createTestOrder();
        existingOrder.setId(orderId);
        BookInstance bookInstance = existingOrder.getBookInstance();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(bookInstanceRepository.save(any(BookInstance.class))).thenReturn(bookInstance);
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        // Act
        orderService.cancelOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).findById(orderId);
        verify(bookInstanceRepository, times(1)).save(bookInstance);
        verify(orderRepository, times(1)).save(existingOrder);
        verify(auditService, times(1)).logOrderOperation(any(User.class), any(Order.class), any(AuditOperation.class), any(), any());
    }

    // Тесты для deleteOrder()
    @Test
    void Should_DeleteOrder_When_OrderExists() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = createTestOrder();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_DeletingNonExistentOrder() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            orderService.deleteOrder(nonExistentId);
        });
        verify(orderRepository, times(1)).findById(nonExistentId);
        verify(orderRepository, never()).delete(any(Order.class));
    }
}