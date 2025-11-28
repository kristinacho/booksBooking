package com.books.controllers;

import com.books.dto.BookDTO;
import com.books.dto.CreateBookDTO;
import com.books.dto.UpdateBookDTO;
import com.books.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private UUID bookId;
    private BookDTO sampleBook;
    private CreateBookDTO createBookDTO;
    private UpdateBookDTO updateBookDTO;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        sampleBook = createSampleBookDTO(bookId);
        createBookDTO = createSampleCreateBookDTO();
        updateBookDTO = createSampleUpdateBookDTO();
    }

    private BookDTO createSampleBookDTO(UUID id) {
        BookDTO book = new BookDTO();
        book.setId(id);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setPublicationYear(2023);
        return book;
    }

    private CreateBookDTO createSampleCreateBookDTO() {
        CreateBookDTO book = new CreateBookDTO();
        book.setTitle("New Book");
        book.setAuthor("New Author");
        book.setPublicationYear(2024);
        return book;
    }

    private UpdateBookDTO createSampleUpdateBookDTO() {
        UpdateBookDTO book = new UpdateBookDTO();
        book.setTitle("Updated Book");
        book.setAuthor("Updated Author");
        book.setPublicationYear(2024);
        return book;
    }

    @Test
    void getAllBooks_WhenNoFilters_ShouldReturnAllBooks() {
        // Given
        List<BookDTO> expectedBooks = Arrays.asList(sampleBook, createSampleBookDTO(UUID.randomUUID()));
        when(bookService.findAll()).thenReturn(expectedBooks);

        // When
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks(null, null, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService).findAll();
        verify(bookService, never()).searchBooks(any(), any(), any());
    }

    @Test
    void getAllBooks_WhenTitleFilterProvided_ShouldReturnFilteredBooks() {
        // Given
        String title = "Test";
        List<BookDTO> expectedBooks = Collections.singletonList(sampleBook);
        when(bookService.searchBooks(eq(title), any(), any())).thenReturn(expectedBooks);

        // When
        ResponseEntity<List<BookDTO>> response = bookController.getAllBooks(title, null, null);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService).searchBooks(title, null, null);
        verify(bookService, never()).findAll();
    }

    @Test
    void getBookById_WithValidId_ShouldReturnBook() {
        // Given
        when(bookService.findById(bookId)).thenReturn(sampleBook);

        // When
        ResponseEntity<BookDTO> response = bookController.getBookById(bookId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
        verify(bookService).findById(bookId);
    }

    @Test
    void getBooksByTitle_WithValidTitle_ShouldReturnBooks() {
        // Given
        String title = "Test";
        List<BookDTO> expectedBooks = Collections.singletonList(sampleBook);
        when(bookService.findByTitle(title)).thenReturn(expectedBooks);

        // When
        ResponseEntity<List<BookDTO>> response = bookController.getBooksByTitle(title);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService).findByTitle(title);
    }

    @Test
    void getBooksByAuthor_WithValidAuthor_ShouldReturnBooks() {
        // Given
        String author = "Test Author";
        List<BookDTO> expectedBooks = Collections.singletonList(sampleBook);
        when(bookService.findByAuthor(author)).thenReturn(expectedBooks);

        // When
        ResponseEntity<List<BookDTO>> response = bookController.getBooksByAuthor(author);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService).findByAuthor(author);
    }

    @Test
    void getBooksByYear_WithValidYear_ShouldReturnBooks() {
        // Given
        Integer year = 2023;
        List<BookDTO> expectedBooks = Collections.singletonList(sampleBook);
        when(bookService.findByPublicationYear(year)).thenReturn(expectedBooks);

        // When
        ResponseEntity<List<BookDTO>> response = bookController.getBooksByYear(year);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedBooks, response.getBody());
        verify(bookService).findByPublicationYear(year);
    }

    @Test
    void createBook_WithValidData_ShouldReturnCreatedBook() {
        // Given
        when(bookService.createBook(createBookDTO)).thenReturn(sampleBook);

        // When
        ResponseEntity<BookDTO> response = bookController.createBook(createBookDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleBook, response.getBody());
        verify(bookService).createBook(createBookDTO);
    }

    @Test
    void updateBook_WithValidData_ShouldReturnUpdatedBook() {
        // Given
        BookDTO updatedBook = createSampleBookDTO(bookId);
        updatedBook.setTitle("Updated Title");
        when(bookService.updateBook(eq(bookId), any(UpdateBookDTO.class))).thenReturn(updatedBook);

        // When
        ResponseEntity<BookDTO> response = bookController.updateBook(bookId, updateBookDTO);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());
        verify(bookService).updateBook(bookId, updateBookDTO);
    }

    @Test
    void deleteBook_WithValidId_ShouldCallDeleteService() {
        // When
        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).deleteBook(bookId);
    }
}