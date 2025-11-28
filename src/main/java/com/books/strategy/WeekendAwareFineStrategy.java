package com.books.strategy;

import java.time.LocalDateTime;

public class WeekendAwareFineStrategy implements FineCalculationStrategy {
    @Override
    public double calculateFine(LocalDateTime expectedReturn, LocalDateTime actualReturn, double baseFine) {
        if (actualReturn.isBefore(expectedReturn) || actualReturn.isEqual(expectedReturn)) {
            return 0.0;
        }

        long businessDaysOverdue = calculateBusinessDays(expectedReturn, actualReturn);
        return businessDaysOverdue * baseFine;
    }

    private long calculateBusinessDays(LocalDateTime start, LocalDateTime end) {
        long businessDays = 0;
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            java.time.DayOfWeek day = current.getDayOfWeek();
            if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
                businessDays++;
            }
            current = current.plusDays(1);
        }

        return businessDays;
    }
}