package com.books.services;

import com.books.dto.CreateLibraryDTO;
import com.books.dto.LibraryDTO;
import com.books.dto.UpdateLibraryDTO;
import com.books.entities.Library;
import com.books.exceptions.EntityNotFoundException;
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
class LibraryServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Library createTestLibrary() {
        Library library = new Library();
        library.setId(UUID.randomUUID());
        library.setName("Test Library");
        library.setAddress("Test Address");
        library.setWorkingHours("9:00-18:00");
        return library;
    }

    private CreateLibraryDTO createTestCreateLibraryDTO() {
        CreateLibraryDTO dto = new CreateLibraryDTO();
        dto.setName("New Library");
        dto.setAddress("New Address");
        dto.setWorkingHours("10:00-20:00");
        return dto;
    }

    // Тесты для findAll()
    @Test
    void Should_ReturnAllLibraries_When_LibrariesExist() {
        // Arrange
        Library library = createTestLibrary();
        when(libraryRepository.findAll()).thenReturn(List.of(library));

        // Act
        List<LibraryDTO> result = libraryService.findAll();

        // Assert
        assertEquals(1, result.size());
        verify(libraryRepository, times(1)).findAll();
    }

    @Test
    void Should_ReturnEmptyList_When_NoLibrariesExist() {
        // Arrange
        when(libraryRepository.findAll()).thenReturn(List.of());

        // Act
        List<LibraryDTO> result = libraryService.findAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(libraryRepository, times(1)).findAll();
    }

    // Тесты для findById()
    @Test
    void Should_ReturnLibrary_When_LibraryWithIdExists() {
        // Arrange
        UUID libraryId = UUID.randomUUID();
        Library library = createTestLibrary();
        library.setId(libraryId);
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        // Act
        LibraryDTO result = libraryService.findById(libraryId);

        // Assert
        assertNotNull(result);
        assertEquals(libraryId, result.getId());
        assertEquals("Test Library", result.getName());
        verify(libraryRepository, times(1)).findById(libraryId);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_LibraryWithIdNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(libraryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            libraryService.findById(nonExistentId);
        });
        verify(libraryRepository, times(1)).findById(nonExistentId);
    }

    // Тесты для findByName()
    @Test
    void Should_ReturnLibraries_When_LibrariesWithNameExist() {
        // Arrange
        Library library = createTestLibrary();
        when(libraryRepository.findByNameContainingIgnoreCase("test")).thenReturn(List.of(library));

        // Act
        List<LibraryDTO> result = libraryService.findByName("test");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Library", result.get(0).getName());
        verify(libraryRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    // Тесты для findByAddress()
    @Test
    void Should_ReturnLibraries_When_LibrariesWithAddressExist() {
        // Arrange
        Library library = createTestLibrary();
        when(libraryRepository.findByAddressContainingIgnoreCase("address")).thenReturn(List.of(library));

        // Act
        List<LibraryDTO> result = libraryService.findByAddress("address");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Address", result.get(0).getAddress());
        verify(libraryRepository, times(1)).findByAddressContainingIgnoreCase("address");
    }

    // Тесты для createLibrary()
    @Test
    void Should_CreateLibrary_When_ValidDataProvided() {
        // Arrange
        CreateLibraryDTO createDTO = createTestCreateLibraryDTO();
        Library savedLibrary = createTestLibrary();
        savedLibrary.setName("New Library");

        when(libraryRepository.save(any(Library.class))).thenReturn(savedLibrary);

        // Act
        LibraryDTO result = libraryService.createLibrary(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Library", result.getName());
        verify(libraryRepository, times(1)).save(any(Library.class));
    }

    // Тесты для updateLibrary()
    @Test
    void Should_UpdateLibrary_When_ValidDataProvided() {
        // Arrange
        UUID libraryId = UUID.randomUUID();
        Library existingLibrary = createTestLibrary();
        existingLibrary.setId(libraryId);

        UpdateLibraryDTO updateDTO = new UpdateLibraryDTO();
        updateDTO.setName("Updated Library");
        updateDTO.setAddress("Updated Address");

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(existingLibrary));
        when(libraryRepository.save(any(Library.class))).thenReturn(existingLibrary);

        // Act
        LibraryDTO result = libraryService.updateLibrary(libraryId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(libraryRepository, times(1)).findById(libraryId);
        verify(libraryRepository, times(1)).save(existingLibrary);
    }

    @Test
    void Should_UpdateOnlyProvidedFields_When_PartialDataProvided() {
        // Arrange
        UUID libraryId = UUID.randomUUID();
        Library existingLibrary = createTestLibrary();
        existingLibrary.setId(libraryId);

        UpdateLibraryDTO updateDTO = new UpdateLibraryDTO();
        updateDTO.setName("Updated Name Only");
        // address and workingHours are null

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(existingLibrary));
        when(libraryRepository.save(any(Library.class))).thenReturn(existingLibrary);

        // Act
        LibraryDTO result = libraryService.updateLibrary(libraryId, updateDTO);

        // Assert
        assertNotNull(result);
        verify(libraryRepository, times(1)).findById(libraryId);
        verify(libraryRepository, times(1)).save(existingLibrary);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_UpdatingNonExistentLibrary() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        UpdateLibraryDTO updateDTO = new UpdateLibraryDTO();
        updateDTO.setName("Updated Library");

        when(libraryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            libraryService.updateLibrary(nonExistentId, updateDTO);
        });
        verify(libraryRepository, times(1)).findById(nonExistentId);
        verify(libraryRepository, never()).save(any(Library.class));
    }

    // Тесты для deleteLibrary()
    @Test
    void Should_DeleteLibrary_When_LibraryExists() {
        // Arrange
        UUID libraryId = UUID.randomUUID();
        Library library = createTestLibrary();
        library.setId(libraryId);

        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        // Act
        libraryService.deleteLibrary(libraryId);

        // Assert
        verify(libraryRepository, times(1)).findById(libraryId);
        verify(libraryRepository, times(1)).delete(library);
    }

    @Test
    void Should_ThrowEntityNotFoundException_When_DeletingNonExistentLibrary() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(libraryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            libraryService.deleteLibrary(nonExistentId);
        });
        verify(libraryRepository, times(1)).findById(nonExistentId);
        verify(libraryRepository, never()).delete(any(Library.class));
    }
}