package com.david.ecommerce.notification.exception;

public class NotificationNotFoundException extends NotificationDomainException {

    public NotificationNotFoundException(String message) {
        super(message);
    }
}
