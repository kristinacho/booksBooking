package com.books.notifications;

import com.books.external.ExternalSMSService;

public class SMSServiceAdapter implements Notification {
    private final ExternalSMSService externalService;
    private final String phoneNumber;
    private final String message;

    public SMSServiceAdapter(ExternalSMSService externalService,
                             String phoneNumber, String message) {
        this.externalService = externalService;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    @Override
    public void send() {
        externalService.sendSMS(phoneNumber, message);
    }

    @Override
    public String getType() {
        return "SMS_ADAPTER";
    }

    @Override
    public String getMessage() {
        return message;
    }
}