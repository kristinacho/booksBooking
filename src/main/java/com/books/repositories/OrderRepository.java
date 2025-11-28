package com.books.repositories;

import com.books.entities.Order;
import com.books.entities.OrderStatus;
import com.books.entities.User;
import com.books.entities.BookInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // Поиск заказов пользователя
    List<Order> findByUser(User user);

    // Поиск заказов по статусу
    List<Order> findByStatus(OrderStatus status);

    // Поиск заказов по экземпляру книги
    List<Order> findByBookInstance(BookInstance bookInstance);

    // Поиск активных заказов пользователя
    List<Order> findByUserAndStatusIn(User user, List<OrderStatus> statuses);
}