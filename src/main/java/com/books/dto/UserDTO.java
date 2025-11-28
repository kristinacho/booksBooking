package com.books.dto;

import com.books.entities.UserRole;
import com.books.entities.UserStatus;

import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private UserStatus status;
    private UserRole role;

    public UserDTO() {}

    public UserDTO(UUID id, String fullName, String email, String phone, UserStatus status, UserRole role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.role = role;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}