package com.books.builders;

import com.books.entities.*;
import java.time.LocalDateTime;

public class OrderBuilder {
    private User user;
    private BookInstance bookInstance;
    private LocalDateTime reservationDeadline;
    private LocalDateTime expectedReturnDate;
    private OrderStatus status = OrderStatus.CREATED;
    private LocalDateTime actualIssueDate;
    private LocalDateTime actualReturnDate;

    public OrderBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public OrderBuilder setBookInstance(BookInstance bookInstance) {
        this.bookInstance = bookInstance;
        return this;
    }

    public OrderBuilder setReservationDeadline(LocalDateTime reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
        return this;
    }

    public OrderBuilder setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
        return this;
    }

    public OrderBuilder setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder setActualIssueDate(LocalDateTime actualIssueDate) {
        this.actualIssueDate = actualIssueDate;
        return this;
    }

    public OrderBuilder setActualReturnDate(LocalDateTime actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        return this;
    }

    public Order build() {
        if (user == null) {
            throw new IllegalStateException("User is required");
        }
        if (bookInstance == null) {
            throw new IllegalStateException("BookInstance is required");
        }

        Order order = new Order();
        order.setUser(user);
        order.setBookInstance(bookInstance);
        order.setReservationDeadline(reservationDeadline);
        order.setExpectedReturnDate(expectedReturnDate);
        order.setStatus(status);
        order.setActualIssueDate(actualIssueDate);
        order.setActualReturnDate(actualReturnDate);

        return order;
    }

    public static OrderBuilder create() {
        return new OrderBuilder();
    }
}