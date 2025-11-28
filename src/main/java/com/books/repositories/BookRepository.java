package com.books.repositories;

import com.books.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    // Поиск книг по автору (игнорируя регистр)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Поиск книг по названию (игнорируя регистр)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Поиск книг по году издания
    List<Book> findByPublicationYear(Integer publicationYear);

    // Поиск книг по автору и году
    List<Book> findByAuthorAndPublicationYear(String author, Integer publicationYear);
}