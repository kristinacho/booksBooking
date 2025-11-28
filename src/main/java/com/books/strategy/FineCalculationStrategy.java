package com.books.strategy;

import java.time.LocalDateTime;

public interface FineCalculationStrategy {
    double calculateFine(LocalDateTime expectedReturn, LocalDateTime actualReturn, double baseFine);
}