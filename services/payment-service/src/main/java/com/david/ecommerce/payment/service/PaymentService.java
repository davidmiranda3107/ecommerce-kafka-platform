package com.david.ecommerce.payment.service;

import com.david.ecommerce.payment.dto.PaymentConfirmRequest;
import com.david.ecommerce.payment.dto.PaymentFailedRequest;
import com.david.ecommerce.payment.dto.PaymentRequest;
import com.david.ecommerce.payment.dto.PaymentResponse;
import com.david.ecommerce.payment.kafka.event.OrderCreatedEvent;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getPaymentsByOrderId(Long orderId);
    PaymentResponse markPaymentAsComplete(PaymentConfirmRequest request);
    PaymentResponse markPaymentAsFailed(PaymentFailedRequest request);
    void processPayment(OrderCreatedEvent event);
}
