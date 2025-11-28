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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final AuditService auditService;
    private final ApplicationConfig config; // Singleton

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        BookInstanceRepository bookInstanceRepository, AuditService auditService,
                        ApplicationConfig config) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookInstanceRepository = bookInstanceRepository;
        this.auditService = auditService;
        this.config = config;
    }

    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO findById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id: " + id + " не найден"));
        return convertToDTO(order);
    }

    public List<OrderDTO> findByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));
        return orderRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> findActiveByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + userId + " не найден"));
        List<OrderStatus> activeStatuses = List.of(OrderStatus.CREATED, OrderStatus.READY_FOR_ISSUE, OrderStatus.ISSUED);
        return orderRepository.findByUserAndStatusIn(user, activeStatuses).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(CreateOrderDTO orderDTO) {
        System.out.println("Create Order for user: " + orderDTO.getUserId() + " and book instance: " + orderDTO.getBookInstanceId());

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id: " + orderDTO.getUserId() + " не найден"));

        BookInstance bookInstance = bookInstanceRepository.findById(orderDTO.getBookInstanceId())
                .orElseThrow(() -> new EntityNotFoundException("Экземпляр книги с id: " + orderDTO.getBookInstanceId() + " не найден"));

        if (bookInstance.getStatus() != BookInstanceStatus.AVAILABLE) {
            throw new RuntimeException("Экземпляр книги недоступен для заказа");
        }

        Order order = new Order();
        order.setUser(user);
        order.setBookInstance(bookInstance);
        order.setReservationDeadline(orderDTO.getReservationDeadline());
        order.setExpectedReturnDate(orderDTO.getExpectedReturnDate());
        order.setStatus(OrderStatus.CREATED);

        bookInstance.setStatus(BookInstanceStatus.RESERVED);
        bookInstanceRepository.save(bookInstance);

        Order savedOrder = orderRepository.save(order);

        auditService.logOrderOperation(user, savedOrder, AuditOperation.ORDER,
                null, "Order created with status: " + OrderStatus.CREATED);

        return convertToDTO(savedOrder);
    }

    // НОВЫЙ МЕТОД с Builder Pattern и Singleton
    public OrderDTO createOrderWithBuilder(CreateOrderDTO orderDTO) {
        System.out.println("Create Order with Builder for user: " + orderDTO.getUserId());

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        BookInstance bookInstance = bookInstanceRepository.findById(orderDTO.getBookInstanceId())
                .orElseThrow(() -> new EntityNotFoundException("Экземпляр книги не найден"));

        // Проверка ограничений из Singleton конфигурации
        List<Order> activeOrders = orderRepository.findByUserAndStatusIn(
                user,
                List.of(OrderStatus.CREATED, OrderStatus.READY_FOR_ISSUE, OrderStatus.ISSUED)
        );
//singleton
        if (activeOrders.size() >= config.getMaxActiveOrdersPerUser()) {
            throw new RuntimeException("Превышено максимальное количество активных заказов: " +
                    config.getMaxActiveOrdersPerUser());
        }

        if (bookInstance.getStatus() != BookInstanceStatus.AVAILABLE) {
            throw new RuntimeException("Экземпляр книги недоступен для заказа");
        }

        // Использование Builder Pattern для создания заказа
        Order order = OrderBuilder.create()
                .setUser(user)
                .setBookInstance(bookInstance)
                .setReservationDeadline(LocalDateTime.now().plusDays(config.getReservationPeriodDays()))
                .setExpectedReturnDate(orderDTO.getExpectedReturnDate())
                .setStatus(OrderStatus.CREATED)
                .build();

        bookInstance.setStatus(BookInstanceStatus.RESERVED);
        bookInstanceRepository.save(bookInstance);

        Order savedOrder = orderRepository.save(order);

        auditService.logOrderOperation(user, savedOrder, AuditOperation.ORDER,
                null, "Order created with Builder pattern");

        return convertToDTO(savedOrder);
    }

    public OrderDTO updateOrder(UUID id, UpdateOrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id: " + id + " не найден"));

        OrderStatus oldStatus = order.getStatus();

        if (orderDTO.getReservationDeadline() != null) {
            order.setReservationDeadline(orderDTO.getReservationDeadline());
        }
        if (orderDTO.getExpectedReturnDate() != null) {
            order.setExpectedReturnDate(orderDTO.getExpectedReturnDate());
        }
        if (orderDTO.getActualIssueDate() != null) {
            order.setActualIssueDate(orderDTO.getActualIssueDate());
        }
        if (orderDTO.getActualReturnDate() != null) {
            order.setActualReturnDate(orderDTO.getActualReturnDate());
        }
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());

            if (orderDTO.getStatus() == OrderStatus.RETURNED || orderDTO.getStatus() == OrderStatus.CANCELLED) {
                BookInstance bookInstance = order.getBookInstance();
                bookInstance.setStatus(BookInstanceStatus.AVAILABLE);
                bookInstanceRepository.save(bookInstance);
            }
        }

        Order savedOrder = orderRepository.save(order);

        if (orderDTO.getStatus() != null && orderDTO.getStatus() != oldStatus) {
            auditService.logOrderOperation(order.getUser(), savedOrder, AuditOperation.UPDATE,
                    "Status: " + oldStatus, "Status: " + orderDTO.getStatus());
        }

        return convertToDTO(savedOrder);
    }

    public OrderDTO updateOrderStatus(UUID id, OrderStatus status) {
        UpdateOrderDTO orderDTO = new UpdateOrderDTO();
        orderDTO.setStatus(status);

        if (status == OrderStatus.ISSUED) {
            orderDTO.setActualIssueDate(LocalDateTime.now());
        } else if (status == OrderStatus.RETURNED) {
            orderDTO.setActualReturnDate(LocalDateTime.now());
        }

        return updateOrder(id, orderDTO);
    }

    public void cancelOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id: " + id + " не найден"));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);

        BookInstance bookInstance = order.getBookInstance();
        bookInstance.setStatus(BookInstanceStatus.AVAILABLE);
        bookInstanceRepository.save(bookInstance);

        orderRepository.save(order);

        auditService.logOrderOperation(order.getUser(), order, AuditOperation.CANCEL,
                "Status: " + oldStatus, "Status: " + OrderStatus.CANCELLED);
    }

    public void deleteOrder(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с id: " + id + " не найден"));
        orderRepository.delete(order);
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getFullName(),
                order.getBookInstance().getId(),
                order.getBookInstance().getBook().getTitle(),
                order.getBookInstance().getBook().getAuthor(),
                order.getBookInstance().getLibrary().getId(),
                order.getBookInstance().getLibrary().getName(),
                order.getCreatedAt(),
                order.getReservationDeadline(),
                order.getActualIssueDate(),
                order.getExpectedReturnDate(),
                order.getActualReturnDate(),
                order.getStatus()
        );
    }
}