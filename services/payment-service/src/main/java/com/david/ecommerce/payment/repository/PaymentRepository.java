package com.david.ecommerce.payment.repository;

import com.david.ecommerce.payment.domain.Payment;
import com.david.ecommerce.payment.domain.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);
    List<Payment> findByStatus(PaymentStatus status);
    boolean existsByOrderId(Long orderId);
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
