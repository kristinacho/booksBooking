package com.books.notifications;

import com.books.external.ExternalEmailService;

public class EmailServiceAdapter implements Notification {
    private final ExternalEmailService externalService;
    private final String recipient;
    private final String subject;
    private final String message;

    public EmailServiceAdapter(ExternalEmailService externalService,
                               String recipient, String subject, String message) {
        this.externalService = externalService;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }

    @Override
    public void send() {
        externalService.sendEmail(recipient, subject, message);
    }

    @Override
    public String getType() {
        return "EMAIL_ADAPTER";
    }

    @Override
    public String getMessage() {
        return message;
    }
}