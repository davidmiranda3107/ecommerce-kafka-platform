package com.david.ecommerce.payment.exception;

public class PaymentNotFoundException extends PaymentDomainException {

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(Long id) {
        super("Payment not found: " + id);
    }
}
