package com.david.ecommerce.inventory.domain.entities;

import com.david.ecommerce.inventory.domain.enums.InventoryMovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long productId;

    @Column
    private Integer quantityChange;

    @Enumerated(EnumType.STRING)
    private InventoryMovementType type;

    @Column
    private String reason;

    @Column
    private String reference;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
