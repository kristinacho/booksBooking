package com.books.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateOrderDTO {
    private UUID userId;
    private UUID bookInstanceId;
    private LocalDateTime reservationDeadline;
    private LocalDateTime expectedReturnDate;

    public CreateOrderDTO() {}

    public CreateOrderDTO(UUID userId, UUID bookInstanceId, LocalDateTime reservationDeadline, LocalDateTime expectedReturnDate) {
        this.userId = userId;
        this.bookInstanceId = bookInstanceId;
        this.reservationDeadline = reservationDeadline;
        this.expectedReturnDate = expectedReturnDate;
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public UUID getBookInstanceId() { return bookInstanceId; }
    public void setBookInstanceId(UUID bookInstanceId) { this.bookInstanceId = bookInstanceId; }

    public LocalDateTime getReservationDeadline() { return reservationDeadline; }
    public void setReservationDeadline(LocalDateTime reservationDeadline) { this.reservationDeadline = reservationDeadline; }

    public LocalDateTime getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
}