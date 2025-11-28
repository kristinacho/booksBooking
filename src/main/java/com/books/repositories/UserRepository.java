package com.books.repositories;

import com.books.entities.User;
import com.books.entities.UserRole;
import com.books.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Поиск пользователя по email
    Optional<User> findByEmail(String email);

    // Поиск пользователей по роли
    List<User> findByRole(UserRole role);

    // Поиск пользователей по статусу
    List<User> findByStatus(UserStatus status);

    // Поиск по имени (игнорируя регистр)
    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // Проверка существования пользователя с email
    boolean existsByEmail(String email);
}