package com.books.dto;

import com.books.entities.BookInstanceStatus;

public class UpdateBookInstanceDTO {
    private BookInstanceStatus status;

    public UpdateBookInstanceDTO() {}

    public UpdateBookInstanceDTO(BookInstanceStatus status) {
        this.status = status;
    }

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }
}