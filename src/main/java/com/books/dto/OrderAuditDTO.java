package com.books.dto;

import com.books.entities.AuditOperation;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderAuditDTO {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID userId;
    private String userFullName;
    private AuditOperation operation;
    private String oldValue;
    private String newValue;
    private UUID orderId;

    public OrderAuditDTO() {}

    public OrderAuditDTO(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt,
                         UUID userId, String userFullName, AuditOperation operation,
                         String oldValue, String newValue, UUID orderId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.userFullName = userFullName;
        this.operation = operation;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.orderId = orderId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public AuditOperation getOperation() { return operation; }
    public void setOperation(AuditOperation operation) { this.operation = operation; }

    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
}