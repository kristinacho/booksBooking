package com.books.dto;

import com.books.entities.UserStatus;
import jakarta.validation.constraints.Email;

public class UpdateUserDTO {
    private String fullName;

    @Email(message = "Некорректный формат email")
    private String email;

    private String phone;
    private UserStatus status;

    public UpdateUserDTO() {}

    public UpdateUserDTO(String fullName, String email, String phone, UserStatus status) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}