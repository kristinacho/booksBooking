package com.books.controllers;

import com.books.dto.BookInstanceDTO;
import com.books.dto.CreateBookInstanceDTO;
import com.books.dto.UpdateBookInstanceDTO;
import com.books.entities.BookInstanceStatus;
import com.books.services.BookInstanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/book-instances")
public class BookInstanceController {

    private final BookInstanceService bookInstanceService;

    public BookInstanceController(BookInstanceService bookInstanceService) {
        this.bookInstanceService = bookInstanceService;
    }

    @GetMapping
    public ResponseEntity<List<BookInstanceDTO>> getAllBookInstances() {
        List<BookInstanceDTO> bookInstances = bookInstanceService.findAll();
        return ResponseEntity.ok(bookInstances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookInstanceDTO> getBookInstanceById(@PathVariable UUID id) {
        BookInstanceDTO bookInstance = bookInstanceService.findById(id);
        return ResponseEntity.ok(bookInstance);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookInstanceDTO>> getBookInstancesByBook(@PathVariable UUID bookId) {
        List<BookInstanceDTO> bookInstances = bookInstanceService.findByBookId(bookId);
        return ResponseEntity.ok(bookInstances);
    }

    @GetMapping("/library/{libraryId}")
    public ResponseEntity<List<BookInstanceDTO>> getBookInstancesByLibrary(@PathVariable UUID libraryId) {
        List<BookInstanceDTO> bookInstances = bookInstanceService.findByLibraryId(libraryId);
        return ResponseEntity.ok(bookInstances);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookInstanceDTO>> getBookInstancesByStatus(@PathVariable BookInstanceStatus status) {
        List<BookInstanceDTO> bookInstances = bookInstanceService.findByStatus(status);
        return ResponseEntity.ok(bookInstances);
    }

    @GetMapping("/library/{libraryId}/available")
    public ResponseEntity<List<BookInstanceDTO>> getAvailableBookInstancesByLibrary(@PathVariable UUID libraryId) {
        List<BookInstanceDTO> bookInstances = bookInstanceService.findAvailableByLibrary(libraryId);
        return ResponseEntity.ok(bookInstances);
    }

    @PostMapping
    public ResponseEntity<BookInstanceDTO> createBookInstance(@RequestBody CreateBookInstanceDTO bookInstanceDTO) {
        BookInstanceDTO createdBookInstance = bookInstanceService.createBookInstance(bookInstanceDTO);
        return ResponseEntity.ok(createdBookInstance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookInstanceDTO> updateBookInstance(@PathVariable UUID id, @RequestBody UpdateBookInstanceDTO bookInstanceDTO) {
        BookInstanceDTO updatedBookInstance = bookInstanceService.updateBookInstance(id, bookInstanceDTO);
        return ResponseEntity.ok(updatedBookInstance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookInstance(@PathVariable UUID id) {
        bookInstanceService.deleteBookInstance(id);
        return ResponseEntity.ok().build();
    }
}