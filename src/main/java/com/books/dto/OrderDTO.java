package com.books.dto;

import com.books.entities.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderDTO {
    private UUID id;
    private UUID userId;
    private String userFullName;
    private UUID bookInstanceId;
    private String bookTitle;
    private String bookAuthor;
    private UUID libraryId;
    private String libraryName;
    private LocalDateTime createdAt;
    private LocalDateTime reservationDeadline;
    private LocalDateTime actualIssueDate;
    private LocalDateTime expectedReturnDate;
    private LocalDateTime actualReturnDate;
    private OrderStatus status;

    public OrderDTO() {}

    public OrderDTO(UUID id, UUID userId, String userFullName, UUID bookInstanceId,
                    String bookTitle, String bookAuthor, UUID libraryId, String libraryName,
                    LocalDateTime createdAt, LocalDateTime reservationDeadline,
                    LocalDateTime actualIssueDate, LocalDateTime expectedReturnDate,
                    LocalDateTime actualReturnDate, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.bookInstanceId = bookInstanceId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.libraryId = libraryId;
        this.libraryName = libraryName;
        this.createdAt = createdAt;
        this.reservationDeadline = reservationDeadline;
        this.actualIssueDate = actualIssueDate;
        this.expectedReturnDate = expectedReturnDate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public UUID getBookInstanceId() { return bookInstanceId; }
    public void setBookInstanceId(UUID bookInstanceId) { this.bookInstanceId = bookInstanceId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public UUID getLibraryId() { return libraryId; }
    public void setLibraryId(UUID libraryId) { this.libraryId = libraryId; }

    public String getLibraryName() { return libraryName; }
    public void setLibraryName(String libraryName) { this.libraryName = libraryName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getReservationDeadline() { return reservationDeadline; }
    public void setReservationDeadline(LocalDateTime reservationDeadline) { this.reservationDeadline = reservationDeadline; }

    public LocalDateTime getActualIssueDate() { return actualIssueDate; }
    public void setActualIssueDate(LocalDateTime actualIssueDate) { this.actualIssueDate = actualIssueDate; }

    public LocalDateTime getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public LocalDateTime getActualReturnDate() { return actualReturnDate; }
    public void setActualReturnDate(LocalDateTime actualReturnDate) { this.actualReturnDate = actualReturnDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}