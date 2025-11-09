package com.mytextile.dispatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * This entity tracks the quantity of a finished good
 * that is ready for dispatch for a specific order.
 */
@Data
@Entity
@Table(name = "dispatchable_stock", indexes = {
    @Index(name = "idx_dispatch_order_sku", columnList = "orderId, itemSku", unique = true)
})
public class DispatchableStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private String itemSku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal availableQuantity = BigDecimal.ZERO;
}