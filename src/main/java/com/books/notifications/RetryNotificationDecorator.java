package com.books.notifications;

public class RetryNotificationDecorator extends NotificationDecorator {
    private final int maxRetries;

    public RetryNotificationDecorator(Notification notification, int maxRetries) {
        super(notification);
        this.maxRetries = maxRetries;
    }

    @Override
    public void send() {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                System.out.println("Попытка отправки " + attempt + "/" + maxRetries);
                super.send();
                System.out.println("Уведомление отправлено успешно");
                return;
            } catch (Exception e) {
                System.out.println("Ошибка при отправке: " + e.getMessage());
                if (attempt == maxRetries) {
                    throw new RuntimeException("Не удалось отправить уведомление после " + maxRetries + " попыток");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Поток был прерван", ie);
                }
            }
        }
    }
}