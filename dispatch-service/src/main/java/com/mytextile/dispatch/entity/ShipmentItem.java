package com.mytextile.dispatch.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "shipment_items")
public class ShipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    // Real N:1 relationship back to Shipment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(name = "item_sku", nullable = false)
    private String itemSku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
}