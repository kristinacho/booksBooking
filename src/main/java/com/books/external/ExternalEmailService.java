package com.books.external;

public class ExternalEmailService {
    public void sendEmail(String to, String title, String body) {
        System.out.println("Реальная отправка email через внешний сервис:");
        System.out.println("Кому: " + to);
        System.out.println("Заголовок: " + title);
        System.out.println("Текст: " + body);
    }
}