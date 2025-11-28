package com.books.builders;

import com.books.entities.User;
import com.books.entities.UserRole;
import com.books.entities.UserStatus;

public class UserBuilder {
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private UserRole role = UserRole.READER;
    private UserStatus status = UserStatus.ACTIVE;

    public UserBuilder setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRole(UserRole role) {
        this.role = role;
        return this;
    }

    public UserBuilder setStatus(UserStatus status) {
        this.status = status;
        return this;
    }

    public User build() {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalStateException("Full name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalStateException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException("Password is required");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRole(role);
        user.setStatus(status);

        return user;
    }

    public static UserBuilder create() {
        return new UserBuilder();
    }
}