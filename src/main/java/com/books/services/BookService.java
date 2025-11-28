package com.books.services;

import com.books.dto.BookDTO;
import com.books.dto.CreateBookDTO;
import com.books.dto.UpdateBookDTO;
import com.books.entities.Book;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO findById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id: " + id + " не найдена"));
        return convertToDTO(book);
    }

    public List<BookDTO> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findByPublicationYear(Integer year) {
        return bookRepository.findByPublicationYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> findByAuthorAndYear(String author, Integer year) {
        return bookRepository.findByAuthorAndPublicationYear(author, year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO createBook(CreateBookDTO bookDTO) {
        System.out.println("Create Book " + bookDTO.getTitle() + " " + bookDTO.getAuthor() + " " + bookDTO.getPublicationYear());

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublicationYear(bookDTO.getPublicationYear());

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(UUID id, UpdateBookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id: " + id + " не найдена"));

        if (bookDTO.getTitle() != null) {
            book.setTitle(bookDTO.getTitle());
        }
        if (bookDTO.getAuthor() != null) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getPublicationYear() != null) {
            book.setPublicationYear(bookDTO.getPublicationYear());
        }

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public void deleteBook(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id: " + id + " не найдена"));
        bookRepository.delete(book);
    }

    public List<BookDTO> searchBooks(String title, String author, Integer year) {
        if (title != null && !title.isEmpty()) {
            return findByTitle(title);
        } else if (author != null && !author.isEmpty()) {
            return findByAuthor(author);
        } else if (year != null) {
            return findByPublicationYear(year);
        }
        return findAll();
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear()
        );
    }
}