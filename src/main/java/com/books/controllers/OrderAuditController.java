package com.books.controllers;

import com.books.dto.OrderAuditDTO;
import com.books.entities.AuditOperation;
import com.books.services.OrderAuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-audit")
public class OrderAuditController {

    private final OrderAuditService orderAuditService;

    public OrderAuditController(OrderAuditService orderAuditService) {
        this.orderAuditService = orderAuditService;
    }

    @GetMapping
    public ResponseEntity<List<OrderAuditDTO>> getAllAuditRecords() {
        List<OrderAuditDTO> auditRecords = orderAuditService.findAll();
        return ResponseEntity.ok(auditRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderAuditDTO> getAuditRecordById(@PathVariable UUID id) {
        OrderAuditDTO auditRecord = orderAuditService.findById(id);
        return ResponseEntity.ok(auditRecord);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderAuditDTO>> getAuditRecordsByOrder(@PathVariable UUID orderId) {
        List<OrderAuditDTO> auditRecords = orderAuditService.findByOrderId(orderId);
        return ResponseEntity.ok(auditRecords);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderAuditDTO>> getAuditRecordsByUser(@PathVariable UUID userId) {
        List<OrderAuditDTO> auditRecords = orderAuditService.findByUserId(userId);
        return ResponseEntity.ok(auditRecords);
    }

    @GetMapping("/operation/{operation}")
    public ResponseEntity<List<OrderAuditDTO>> getAuditRecordsByOperation(@PathVariable AuditOperation operation) {
        List<OrderAuditDTO> auditRecords = orderAuditService.findByOperation(operation);
        return ResponseEntity.ok(auditRecords);
    }

    @GetMapping("/order/{orderId}/operation/{operation}")
    public ResponseEntity<List<OrderAuditDTO>> getAuditRecordsByOrderAndOperation(
            @PathVariable UUID orderId, @PathVariable AuditOperation operation) {
        List<OrderAuditDTO> auditRecords = orderAuditService.findByOrderAndOperation(orderId, operation);
        return ResponseEntity.ok(auditRecords);
    }
}