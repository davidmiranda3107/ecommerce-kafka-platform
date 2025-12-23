package com.david.ecommerce.order.repository;

import com.david.ecommerce.order.entities.Order;
import com.david.ecommerce.order.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

    @Query("""
    SELECT o FROM Order o
    JOIN FETCH o.items i
    WHERE i.productId = :productId
    AND o.status = com.david.ecommerce.order.entities.OrderStatus.CREATED
    """)
    List<Order> findPendingOrdersByProductId(Long productId);

    List<Order> findDistinctByStatusAndItems_ProductId(OrderStatus status, Long productId);
}
