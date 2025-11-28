package com.books.dto;

import java.util.UUID;

public class CreateBookInstanceDTO {
    private UUID bookId;
    private UUID libraryId;

    public CreateBookInstanceDTO() {}

    public CreateBookInstanceDTO(UUID bookId, UUID libraryId) {
        this.bookId = bookId;
        this.libraryId = libraryId;
    }

    public UUID getBookId() { return bookId; }
    public void setBookId(UUID bookId) { this.bookId = bookId; }

    public UUID getLibraryId() { return libraryId; }
    public void setLibraryId(UUID libraryId) { this.libraryId = libraryId; }
}