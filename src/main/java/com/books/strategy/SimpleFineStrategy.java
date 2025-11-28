package com.books.strategy;

import java.time.LocalDateTime;

public class SimpleFineStrategy implements FineCalculationStrategy {
    @Override
    public double calculateFine(LocalDateTime expectedReturn, LocalDateTime actualReturn, double baseFine) {
        if (actualReturn.isBefore(expectedReturn) || actualReturn.isEqual(expectedReturn)) {
            return 0.0;
        }

        long daysOverdue = java.time.Duration.between(expectedReturn, actualReturn).toDays();
        return daysOverdue * baseFine;
    }
}