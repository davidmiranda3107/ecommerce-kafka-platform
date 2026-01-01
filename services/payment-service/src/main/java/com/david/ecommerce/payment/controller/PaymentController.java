package com.david.ecommerce.payment.controller;

import com.david.ecommerce.payment.dto.PaymentConfirmRequest;
import com.david.ecommerce.payment.dto.PaymentFailedRequest;
import com.david.ecommerce.payment.dto.PaymentRequest;
import com.david.ecommerce.payment.dto.PaymentResponse;
import com.david.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(paymentService.getPaymentsByOrderId(orderId));
    }

    @PutMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @Valid @RequestBody PaymentConfirmRequest request
            ) {
        return ResponseEntity.ok(paymentService.markPaymentAsComplete(request));
    }

    @PutMapping("/fail")
    public ResponseEntity<PaymentResponse> failPayment(
            @Valid @RequestBody PaymentFailedRequest request
            ) {
        return ResponseEntity.ok(paymentService.markPaymentAsFailed(request));
    }
}
