package com.books.services;

import com.books.dto.BookDTO;
import com.books.dto.CreateBookDTO;
import com.books.dto.UpdateBookDTO;
import com.books.entities.Book;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.BookRepository;
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
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book createTestBook() {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2023);
        return book;
    }

    private CreateBookDTO createTestCreateBookDTO() {
        CreateBookDTO dto = new CreateBookDTO();
        dto.setTitle("New Book");
        dto.setAuthor("New Author");
        dto.setPublicationYear(2024);
        return dto;
    }

    private UpdateBookDTO createTestUpdateBookDTO() {
        UpdateBookDTO dto = new UpdateBookDTO();
        dto.setTitle("Updated Book");
        dto.setAuthor("Updated Author");
        dto.setPublicationYear(2025);
        return dto;
    }

    // Тесты для findAll()
    @Test
    void Should_ReturnAllBooks_When_BooksExist() {
        // Arrange
        Book book1 = createTestBook();
        Book book2 = createTestBook();
        book2.setId(UUID.randomUUID());
        book2.setTitle("Another Book");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Act
        List<BookDTO> result = bookService.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void Should_ReturnEmptyList_When_NoBooksExist() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of());

        // Act
        List<BookDTO> result = bookService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
    }

    // Тесты для findById()
    @Test
    void Should_ReturnBook_When_BookWithIdExists() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        Book book = createTestBook();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        BookDTO result = bookService.findById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Test Book", result.getTitle());
        assertEquals("Test Author", result.getAuthor());
        assertEquals(2023, result.getPublicationYear());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_BookWithIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            bookService.findById(nonExistentId);
        });

        verify(bookRepository, times(1)).findById(nonExistentId);
    }

    // Тесты для findByTitle()
    @Test
    void Should_ReturnBooks_When_BooksWithTitleExist() {
        // Arrange
        Book book = createTestBook();
        when(bookRepository.findByTitleContainingIgnoreCase("test")).thenReturn(List.of(book));

        // Act
        List<BookDTO> result = bookService.findByTitle("test");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).getTitle());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("test");
    }

    @Test
    void Should_ReturnEmptyList_When_NoBooksWithTitleExist() {
        // Arrange
        when(bookRepository.findByTitleContainingIgnoreCase("nonexistent")).thenReturn(List.of());

        // Act
        List<BookDTO> result = bookService.findByTitle("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("nonexistent");
    }

    // Тесты для createBook()
    @Test
    void Should_CreateBook_When_ValidDataProvided() {
        // Arrange
        CreateBookDTO createDTO = createTestCreateBookDTO();
        Book savedBook = createTestBook();
        savedBook.setTitle("New Book");
        savedBook.setAuthor("New Author");
        savedBook.setPublicationYear(2024);

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        // Act
        BookDTO result = bookService.createBook(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Book", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals(2024, result.getPublicationYear());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    // Тесты для updateBook()
    @Test
    void Should_UpdateBook_When_ValidDataProvided() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        Book existingBook = createTestBook();
        existingBook.setId(bookId);

        UpdateBookDTO updateDTO = createTestUpdateBookDTO();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        // Act
        BookDTO result = bookService.updateBook(bookId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void Should_UpdateOnlyProvidedFields_When_PartialDataProvided() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        Book existingBook = createTestBook();
        existingBook.setId(bookId);

        UpdateBookDTO updateDTO = new UpdateBookDTO();
        updateDTO.setTitle("Updated Title Only");
        // author and publicationYear are null

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        // Act
        BookDTO result = bookService.updateBook(bookId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_UpdatingNonExistentBook() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        UpdateBookDTO updateDTO = createTestUpdateBookDTO();

        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            bookService.updateBook(nonExistentId, updateDTO);
        });

        verify(bookRepository, times(1)).findById(nonExistentId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    // Тесты для deleteBook()
    @Test
    void Should_DeleteBook_When_BookExists() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        Book book = createTestBook();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_DeletingNonExistentBook() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            bookService.deleteBook(nonExistentId);
        });

        verify(bookRepository, times(1)).findById(nonExistentId);
        verify(bookRepository, never()).delete(any(Book.class));
    }

    // Тесты для searchBooks()
    @Test
    void Should_SearchByTitle_When_TitleProvided() {
        // Arrange
        Book book = createTestBook();
        when(bookRepository.findByTitleContainingIgnoreCase("test")).thenReturn(List.of(book));

        // Act
        List<BookDTO> result = bookService.searchBooks("test", null, null);

        // Assert
        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("test");
        verify(bookRepository, never()).findByAuthorContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findByPublicationYear(anyInt());
    }

    @Test
    void Should_SearchByAuthor_When_AuthorProvided() {
        // Arrange
        Book book = createTestBook();
        when(bookRepository.findByAuthorContainingIgnoreCase("author")).thenReturn(List.of(book));

        // Act
        List<BookDTO> result = bookService.searchBooks(null, "author", null);

        // Assert
        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findByAuthorContainingIgnoreCase("author");
        verify(bookRepository, never()).findByTitleContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findByPublicationYear(anyInt());
    }

    @Test
    void Should_SearchByYear_When_YearProvided() {
        // Arrange
        Book book = createTestBook();
        when(bookRepository.findByPublicationYear(2023)).thenReturn(List.of(book));

        // Act
        List<BookDTO> result = bookService.searchBooks(null, null, 2023);

        // Assert
        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findByPublicationYear(2023);
        verify(bookRepository, never()).findByTitleContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findByAuthorContainingIgnoreCase(anyString());
    }

    @Test
    void Should_ReturnAllBooks_When_NoSearchCriteriaProvided() {
        // Arrange
        Book book = createTestBook();
        when(bookRepository.findAll()).thenReturn(List.of(book));

        // Act
        List<BookDTO> result = bookService.searchBooks(null, null, null);

        // Assert
        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
        verify(bookRepository, never()).findByTitleContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findByAuthorContainingIgnoreCase(anyString());
        verify(bookRepository, never()).findByPublicationYear(anyInt());
    }
}