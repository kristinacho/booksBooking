package com.books.entities;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book_instances")
public class BookInstance {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "library_id", nullable = false)
    private Library library;        // ID_библиотеки

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;              // ID_книги

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookInstanceStatus status; // статус экземпляра

    public BookInstance() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Library getLibrary() { return library; }
    public void setLibrary(Library library) { this.library = library; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public BookInstanceStatus getStatus() { return status; }
    public void setStatus(BookInstanceStatus status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInstance that = (BookInstance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}