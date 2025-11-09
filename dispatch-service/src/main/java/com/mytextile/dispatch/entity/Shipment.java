package com.mytextile.dispatch.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // Logical reference

    @Column(name = "client_id", nullable = false)
    private Long clientId; // Logical reference

    @Column(name = "challan_number", nullable = false, unique = true)
    private String challanNumber;

    @Lob
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "dispatch_date")
    private LocalDateTime dispatchDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status = ShipmentStatus.PENDING;

    // Real 1:N relationship with ShipmentItem
    @OneToMany(
        mappedBy = "shipment",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ShipmentItem> items = new ArrayList<>();

    public void addItem(ShipmentItem item) {
        this.items.add(item);
    }
}