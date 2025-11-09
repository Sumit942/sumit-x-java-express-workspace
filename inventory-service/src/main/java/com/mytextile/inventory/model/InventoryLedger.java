package com.mytextile.inventory.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_ledger", indexes = {
    @Index(name = "idx_item_id", columnList = "itemId")
})
public class InventoryLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @Column(nullable = false)
    private Long itemId; // Logical reference to InventoryItem

    @Column
    private Long orderId; // Logical reference to Order Service

    @Column
    private Long clientId; // Logical reference to Client Service

    // Positive for stock IN, Negative for stock OUT
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime transactionDate;
}