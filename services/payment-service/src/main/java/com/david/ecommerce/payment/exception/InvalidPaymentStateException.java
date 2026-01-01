package com.david.ecommerce.payment.exception;

public class InvalidPaymentStateException extends PaymentDomainException {

    public InvalidPaymentStateException(String message) {
        super(message);
    }
}
