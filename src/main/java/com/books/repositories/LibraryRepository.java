package com.books.repositories;

import com.books.entities.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LibraryRepository extends JpaRepository<Library, UUID> {

    // Поиск библиотек по названию
    List<Library> findByNameContainingIgnoreCase(String name);

    // Поиск библиотек по адресу
    List<Library> findByAddressContainingIgnoreCase(String address);
}