package com.books.services;

import com.books.dto.CreateLibraryDTO;
import com.books.dto.LibraryDTO;
import com.books.dto.UpdateLibraryDTO;
import com.books.entities.Library;
import com.books.exceptions.EntityNotFoundException;
import com.books.repositories.LibraryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public List<LibraryDTO> findAll() {
        return libraryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LibraryDTO findById(UUID id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Библиотека с id: " + id + " не найдена"));
        return convertToDTO(library);
    }

    public List<LibraryDTO> findByName(String name) {
        return libraryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<LibraryDTO> findByAddress(String address) {
        return libraryRepository.findByAddressContainingIgnoreCase(address).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LibraryDTO createLibrary(CreateLibraryDTO libraryDTO) {
        System.out.println("Create Library " + libraryDTO.getName() + " " + libraryDTO.getAddress() + " " + libraryDTO.getWorkingHours());

        Library library = new Library();
        library.setName(libraryDTO.getName());
        library.setAddress(libraryDTO.getAddress());
        library.setWorkingHours(libraryDTO.getWorkingHours());

        Library savedLibrary = libraryRepository.save(library);
        return convertToDTO(savedLibrary);
    }

    public LibraryDTO updateLibrary(UUID id, UpdateLibraryDTO libraryDTO) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Библиотека с id: " + id + " не найдена"));

        if (libraryDTO.getName() != null) {
            library.setName(libraryDTO.getName());
        }
        if (libraryDTO.getAddress() != null) {
            library.setAddress(libraryDTO.getAddress());
        }
        if (libraryDTO.getWorkingHours() != null) {
            library.setWorkingHours(libraryDTO.getWorkingHours());
        }

        Library savedLibrary = libraryRepository.save(library);
        return convertToDTO(savedLibrary);
    }

    public void deleteLibrary(UUID id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Библиотека с id: " + id + " не найдена"));
        libraryRepository.delete(library);
    }

    private LibraryDTO convertToDTO(Library library) {
        return new LibraryDTO(
                library.getId(),
                library.getName(),
                library.getAddress(),
                library.getWorkingHours()
        );
    }
}