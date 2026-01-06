package com.david.ecommerce.notification.exception;

public class UnsupportedChannelException extends NotificationDomainException {

    public UnsupportedChannelException(String message) {
        super(message);
    }
}
