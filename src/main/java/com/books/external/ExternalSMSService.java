package com.books.external;

public class ExternalSMSService {
    public void sendSMS(String phone, String text) {
        System.out.println("Реальная отправка SMS через внешний сервис:");
        System.out.println("Телефон: " + phone);
        System.out.println("Текст: " + text);
    }
}