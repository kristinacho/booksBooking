package com.books.dto;

import com.books.entities.BookInstanceStatus;
import java.util.UUID;

public class BookInstanceDTO {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private String bookAuthor;
    private UUID libraryId;
    private String libraryName;
    private BookInstanceStatus status;

    public BookInstanceDTO() {}

    public BookInstanceDTO(UUID id, UUID bookId, String bookTitle, String bookAuthor,
                           UUID libraryId, String libraryName, BookInstanceStatus status) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.libraryId = libraryId;
        this.libraryName = libraryName;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getBookId() { return bookId; }
    public void setBookId(UUID bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public UUID getLibraryId() { return libraryId; }
    public void setLibraryId(UUID libraryId) { this.libraryId = libraryId; }

    public String getLibraryName() { return libraryName; }
    public void setLibraryName(String libraryName) { this.libraryName = libraryName; }

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }
}