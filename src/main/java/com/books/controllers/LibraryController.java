package com.books.controllers;

import com.books.dto.CreateLibraryDTO;
import com.books.dto.LibraryDTO;
import com.books.dto.UpdateLibraryDTO;
import com.books.services.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/libraries")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries() {
        List<LibraryDTO> libraries = libraryService.findAll();
        return ResponseEntity.ok(libraries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable UUID id) {
        LibraryDTO library = libraryService.findById(id);
        return ResponseEntity.ok(library);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<LibraryDTO>> getLibrariesByName(@PathVariable String name) {
        List<LibraryDTO> libraries = libraryService.findByName(name);
        return ResponseEntity.ok(libraries);
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<List<LibraryDTO>> getLibrariesByAddress(@PathVariable String address) {
        List<LibraryDTO> libraries = libraryService.findByAddress(address);
        return ResponseEntity.ok(libraries);
    }

    @PostMapping
    public ResponseEntity<LibraryDTO> createLibrary(@RequestBody CreateLibraryDTO libraryDTO) {
        LibraryDTO createdLibrary = libraryService.createLibrary(libraryDTO);
        return ResponseEntity.ok(createdLibrary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryDTO> updateLibrary(@PathVariable UUID id, @RequestBody UpdateLibraryDTO libraryDTO) {
        LibraryDTO updatedLibrary = libraryService.updateLibrary(id, libraryDTO);
        return ResponseEntity.ok(updatedLibrary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable UUID id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.ok().build();
    }
}