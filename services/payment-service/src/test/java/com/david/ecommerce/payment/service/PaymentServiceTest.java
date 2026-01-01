package com.david.ecommerce.payment.service;

import com.david.ecommerce.payment.domain.Payment;
import com.david.ecommerce.payment.domain.PaymentMethod;
import com.david.ecommerce.payment.domain.PaymentStatus;
import com.david.ecommerce.payment.dto.PaymentRequest;
import com.david.ecommerce.payment.dto.PaymentResponse;
import com.david.ecommerce.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void shouldCreatePaymentSuccessfully() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setMethod(PaymentMethod.CREDIT_CARD.name());
        request.setCurrency("USD");

        PaymentResponse response = paymentService.createPayment(request);

        assertNotNull(response.getPaymentId());
        assertEquals(PaymentStatus.PENDING.name(), response.getStatus());

        Payment saved = paymentRepository.findById(response.getPaymentId()).orElseThrow();
        assertEquals(BigDecimal.valueOf(100), saved.getAmount());
    }
}
