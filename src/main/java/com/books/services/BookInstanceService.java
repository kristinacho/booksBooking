package com.books.services;

import com.books.dto.BookInstanceDTO;
import com.books.dto.CreateBookInstanceDTO;
import com.books.dto.UpdateBookInstanceDTO;
import com.books.entities.Book;
import com.books.entities.BookInstance;
import com.books.entities.BookInstanceStatus;
import com.books.entities.Library;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.BookInstanceRepository;
import com.books.repositories.BookRepository;
import com.books.repositories.LibraryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookInstanceService {

    private final BookInstanceRepository bookInstanceRepository;
    private final BookRepository bookRepository;
    private final LibraryRepository libraryRepository;

    public BookInstanceService(BookInstanceRepository bookInstanceRepository,
                               BookRepository bookRepository,
                               LibraryRepository libraryRepository) {
        this.bookInstanceRepository = bookInstanceRepository;
        this.bookRepository = bookRepository;
        this.libraryRepository = libraryRepository;
    }

    public List<BookInstanceDTO> findAll() {
        return bookInstanceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookInstanceDTO findById(UUID id) {
        BookInstance bookInstance = bookInstanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Экземпляр книги с id: " + id + " не найден"));
        return convertToDTO(bookInstance);
    }

    public List<BookInstanceDTO> findByBookId(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Книга с id: " + bookId + " не найдена"));
        return bookInstanceRepository.findByBook(book).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookInstanceDTO> findByLibraryId(UUID libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Библиотека с id: " + libraryId + " не найдена"));
        return bookInstanceRepository.findByLibrary(library).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookInstanceDTO> findByStatus(BookInstanceStatus status) {
        return bookInstanceRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookInstanceDTO> findAvailableByLibrary(UUID libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Библиотека с id: " + libraryId + " не найдена"));
        return bookInstanceRepository.findByLibraryAndStatus(library, BookInstanceStatus.AVAILABLE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookInstanceDTO createBookInstance(CreateBookInstanceDTO bookInstanceDTO) {
        System.out.println("Create BookInstance for book: " + bookInstanceDTO.getBookId() + " in library: " + bookInstanceDTO.getLibraryId());

        Book book = bookRepository.findById(bookInstanceDTO.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Книга с id: " + bookInstanceDTO.getBookId() + " не найдена"));

        Library library = libraryRepository.findById(bookInstanceDTO.getLibraryId())
                .orElseThrow(() -> new EntityNotFoundException("Библиотека с id: " + bookInstanceDTO.getLibraryId() + " не найдена"));

        BookInstance bookInstance = new BookInstance();
        bookInstance.setBook(book);
        bookInstance.setLibrary(library);
        bookInstance.setStatus(BookInstanceStatus.AVAILABLE);

        BookInstance savedBookInstance = bookInstanceRepository.save(bookInstance);
        return convertToDTO(savedBookInstance);
    }

    public BookInstanceDTO updateBookInstance(UUID id, UpdateBookInstanceDTO bookInstanceDTO) {
        BookInstance bookInstance = bookInstanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Экземпляр книги с id: " + id + " не найден"));

        if (bookInstanceDTO.getStatus() != null) {
            bookInstance.setStatus(bookInstanceDTO.getStatus());
        }

        BookInstance savedBookInstance = bookInstanceRepository.save(bookInstance);
        return convertToDTO(savedBookInstance);
    }

    public void deleteBookInstance(UUID id) {
        BookInstance bookInstance = bookInstanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Экземпляр книги с id: " + id + " не найден"));
        bookInstanceRepository.delete(bookInstance);
    }

    private BookInstanceDTO convertToDTO(BookInstance bookInstance) {
        return new BookInstanceDTO(
                bookInstance.getId(),
                bookInstance.getBook().getId(),
                bookInstance.getBook().getTitle(),
                bookInstance.getBook().getAuthor(),
                bookInstance.getLibrary().getId(),
                bookInstance.getLibrary().getName(),
                bookInstance.getStatus()
        );
    }
}