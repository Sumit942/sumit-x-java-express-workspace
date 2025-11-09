package com.mytextile.dispatch.model;

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

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false, unique = true)
    private String challanNumber;

    @Lob
    @Column(nullable = false)
    private String shippingAddress;

    @Column(name = "dispatch_date")
    private LocalDateTime dispatchDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status = ShipmentStatus.PENDING;

    @OneToMany(
        mappedBy = "shipment",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ShipmentItem> items = new ArrayList<>();

    // Helper method
    public void addItem(ShipmentItem item) {
        items.add(item);
        item.setShipment(this);
    }
}