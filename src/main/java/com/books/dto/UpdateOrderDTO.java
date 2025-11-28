package com.books.dto;

import com.books.entities.OrderStatus;
import java.time.LocalDateTime;

public class UpdateOrderDTO {
    private LocalDateTime reservationDeadline;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualIssueDate;
    private LocalDateTime actualReturnDate;
    private OrderStatus status;

    public UpdateOrderDTO() {}

    public UpdateOrderDTO(LocalDateTime reservationDeadline, LocalDateTime expectedReturnDate,
                          LocalDateTime actualIssueDate, LocalDateTime actualReturnDate, OrderStatus status) {
        this.reservationDeadline = reservationDeadline;
        this.expectedReturnDate = expectedReturnDate;
        this.actualIssueDate = actualIssueDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
    }

    public LocalDateTime getReservationDeadline() { return reservationDeadline; }
    public void setReservationDeadline(LocalDateTime reservationDeadline) { this.reservationDeadline = reservationDeadline; }

    public LocalDateTime getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public LocalDateTime getActualIssueDate() { return actualIssueDate; }
    public void setActualIssueDate(LocalDateTime actualIssueDate) { this.actualIssueDate = actualIssueDate; }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}