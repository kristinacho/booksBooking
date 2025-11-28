package com.books.repositories;

import com.books.entities.BookInstance;
import com.books.entities.BookInstanceStatus;
import com.books.entities.Book;
import com.books.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookInstanceRepository extends JpaRepository<BookInstance, UUID> {

    // Поиск экземпляров по книге
    List<BookInstance> findByBook(Book book);

    // Поиск экземпляров по библиотеке
    List<BookInstance> findByLibrary(Library library);

    // Поиск экземпляров по статусу
    List<BookInstance> findByStatus(BookInstanceStatus status);

    // Поиск доступных экземпляров в библиотеке
    List<BookInstance> findByLibraryAndStatus(Library library, BookInstanceStatus status);

    // Поиск экземпляров конкретной книги в библиотеке
    List<BookInstance> findByBookAndLibrary(Book book, Library library);
}