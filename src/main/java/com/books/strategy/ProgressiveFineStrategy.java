package com.books.strategy;

import java.time.LocalDateTime;

public class ProgressiveFineStrategy implements FineCalculationStrategy {
    @Override
    public double calculateFine(LocalDateTime expectedReturn, LocalDateTime actualReturn, double baseFine) {
        if (actualReturn.isBefore(expectedReturn) || actualReturn.isEqual(expectedReturn)) {
            return 0.0;
        }

        long daysOverdue = java.time.Duration.between(expectedReturn, actualReturn).toDays();

        if (daysOverdue <= 7) {
            return daysOverdue * baseFine;
        } else if (daysOverdue <= 30) {
            return daysOverdue * baseFine * 1.5;
        } else {
            return daysOverdue * baseFine * 2.0;
        }
    }
}