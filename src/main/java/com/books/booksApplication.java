package com.books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class booksApplication {

    public static void main(String[] args) {
        SpringApplication.run(booksApplication.class, args);  // ← И здесь!
    }
}