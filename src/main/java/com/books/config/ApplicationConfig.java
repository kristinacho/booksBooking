package com.books.config;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {

    // Конфигурационные параметры (Singleton данные)
    private int reservationPeriodDays = 14;
    private int maxActiveOrdersPerUser = 3;
    private double overdueFinePerDay = 50.0;
    private String libraryName = "Центральная библиотека";
    private String systemEmail = "library@example.com";

    public int getReservationPeriodDays() {
        return reservationPeriodDays;
    }

    public int getMaxActiveOrdersPerUser() {
        return maxActiveOrdersPerUser;
    }

    public double getOverdueFinePerDay() {
        return overdueFinePerDay;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getSystemEmail() {
        return systemEmail;
    }
}