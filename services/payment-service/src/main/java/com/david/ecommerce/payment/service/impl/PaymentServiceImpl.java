package com.david.ecommerce.payment.service.impl;

import com.david.ecommerce.payment.domain.Payment;
import com.david.ecommerce.payment.domain.PaymentMethod;
import com.david.ecommerce.payment.domain.PaymentStatus;
import com.david.ecommerce.payment.dto.PaymentConfirmRequest;
import com.david.ecommerce.payment.dto.PaymentFailedRequest;
import com.david.ecommerce.payment.dto.PaymentRequest;
import com.david.ecommerce.payment.dto.PaymentResponse;
import com.david.ecommerce.payment.exception.PaymentDomainException;
import com.david.ecommerce.payment.exception.PaymentNotFoundException;
import com.david.ecommerce.payment.kafka.event.OrderCreatedEvent;
import com.david.ecommerce.payment.kafka.event.PaymentCompletedEvent;
import com.david.ecommerce.payment.kafka.event.PaymentFailedEvent;
import com.david.ecommerce.payment.kafka.producer.PaymentEventProducer;
import com.david.ecommerce.payment.repository.PaymentRepository;
import com.david.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer eventProducer;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        if (paymentRepository.existsByOrderId(request.getOrderId())) {
            throw new PaymentDomainException("Payment already exists for this order");
        }

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .method(PaymentMethod.valueOf(request.getMethod()))
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        return toResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        return toResponse(findPayment(id));
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse markPaymentAsComplete(PaymentConfirmRequest request) {
        Payment payment = findPayment(request.getPaymentId());

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new PaymentDomainException("Only pending payments can be completed");
        }

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        eventProducer.sendPaymentCompletedEvent(
                new PaymentCompletedEvent(saved.getOrderId(), saved.getId(),
                    saved.getAmount(), saved.getStatus().name())
        );

        return toResponse(saved);
    }

    @Override
    public PaymentResponse markPaymentAsFailed(PaymentFailedRequest request) {
        Payment payment = findPayment(request.getPaymentId());

        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason(request.getReason());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        eventProducer.sendPaymentFailedEvent(
                new PaymentFailedEvent(saved.getOrderId(), saved.getFailureReason())
        );

        return toResponse(saved);
    }

    @Override
    public void processPayment(OrderCreatedEvent event) {

        if (paymentRepository.existsByOrderId(event.getOrderId())) {
            throw new PaymentDomainException("Payment already exists for order: " + event.getOrderId());
        }

        Payment payment = Payment.builder()
                .orderId(event.getOrderId())
                .amount(event.getTotalAmount())
                .currency(event.getCurrency())
                .method(PaymentMethod.valueOf(event.getMethod()))
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        //TO DO: Process payment with gateway
        boolean success = true;

        if (success) {
            this.markPaymentAsComplete(
                    new PaymentConfirmRequest(saved.getId())
            );
        } else {
            this.markPaymentAsFailed(
                    new PaymentFailedRequest(saved.getId(), "Payment rejected by provider")
            );
        }
    }

    private Payment findPayment(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getMethod().name(),
                payment.getStatus().name(),
                payment.getCreatedAt()
        );
    }
}
