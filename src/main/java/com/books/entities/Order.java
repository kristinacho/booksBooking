package com.books.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;              // ID_пользователя

    @ManyToOne
    @JoinColumn(name = "book_instance_id", nullable = false)
    private BookInstance bookInstance; // ID_экземпляра книги

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // дата/время создания

    @Column(name = "reservation_deadline")
    private LocalDateTime reservationDeadline; // срок бронирования

    @Column(name = "actual_issue_date")
    private LocalDateTime actualIssueDate; // дата фактической выдачи

    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate; // дата ожидаемого возврата

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;     // статус заказа

    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate; // дата фактического возврата

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Order() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BookInstance getBookInstance() { return bookInstance; }
    public void setBookInstance(BookInstance bookInstance) { this.bookInstance = bookInstance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReservationDeadline() { return reservationDeadline; }
    public void setReservationDeadline(LocalDateTime reservationDeadline) { this.reservationDeadline = reservationDeadline; }

    public LocalDateTime getActualIssueDate() { return actualIssueDate; }
    public void setActualIssueDate(LocalDateTime actualIssueDate) { this.actualIssueDate = actualIssueDate; }

    public LocalDateTime getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}