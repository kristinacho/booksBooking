package com.books.services;

import com.books.dto.BookInstanceDTO;
import com.books.entities.Book;
import com.books.entities.BookInstance;
import com.books.entities.BookInstanceStatus;
import com.books.entities.Library;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.BookInstanceRepository;
import com.books.repositories.BookRepository;
import com.books.repositories.LibraryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookInstanceServiceTest {

    @Mock
    private BookInstanceRepository bookInstanceRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private BookInstanceService bookInstanceService;

    private Book createTestBook() {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        return book;
    }

    private Library createTestLibrary() {
        Library library = new Library();
        library.setId(UUID.randomUUID());
        library.setName("Test Library");
        library.setAddress("Test Address");
        return library;
    }

    private BookInstance createTestBookInstance() {
        Book book = createTestBook();
        Library library = createTestLibrary();

        BookInstance bookInstance = new BookInstance();
        bookInstance.setId(UUID.randomUUID());
        bookInstance.setBook(book);
        bookInstance.setLibrary(library);
        bookInstance.setStatus(BookInstanceStatus.AVAILABLE);
        return bookInstance;
    }

    @Test
    void Should_ReturnAllBookInstances_When_InstancesExist() {
        // Arrange
        BookInstance bookInstance = createTestBookInstance();
        when(bookInstanceRepository.findAll()).thenReturn(List.of(bookInstance));

        // Act
        List<BookInstanceDTO> result = bookInstanceService.findAll();

        // Assert
        assertEquals(1, result.size());
        verify(bookInstanceRepository, times(1)).findAll();
    }

    @Test
    void Should_ReturnBookInstance_When_InstanceWithIdExists() {
        // Arrange
        UUID instanceId = UUID.randomUUID();
        BookInstance bookInstance = createTestBookInstance();
        bookInstance.setId(instanceId);

        when(bookInstanceRepository.findById(instanceId)).thenReturn(Optional.of(bookInstance));

        // Act
        BookInstanceDTO result = bookInstanceService.findById(instanceId);

        // Assert
        assertNotNull(result);
        assertEquals(instanceId, result.getId());
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_InstanceWithIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(bookInstanceRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            bookInstanceService.findById(nonExistentId);
        });
    }

    @Test
    void Should_ReturnBookInstances_When_BookExists() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        Book book = createTestBook();
        book.setId(bookId);
        BookInstance bookInstance = createTestBookInstance();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookInstanceRepository.findByBook(book)).thenReturn(List.of(bookInstance));

        // Act
        List<BookInstanceDTO> result = bookInstanceService.findByBookId(bookId);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void Should_ReturnBookInstances_When_StatusExists() {
        // Arrange
        BookInstance bookInstance = createTestBookInstance();
        when(bookInstanceRepository.findByStatus(BookInstanceStatus.AVAILABLE)).thenReturn(List.of(bookInstance));

        // Act
        List<BookInstanceDTO> result = bookInstanceService.findByStatus(BookInstanceStatus.AVAILABLE);

        // Assert
        assertEquals(1, result.size());
        assertEquals(BookInstanceStatus.AVAILABLE, result.get(0).getStatus());
    }
}