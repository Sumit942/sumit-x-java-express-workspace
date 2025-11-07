package com.mytextile.production.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "production_jobs")
public class ProductionJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // Logical reference to Order Service

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @OneToMany(
        mappedBy = "productionJob",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ProductionJobInput> inputs = new ArrayList<>();

    @Column(name = "output_item_sku", nullable = false)
    private String outputItemSku; // e.g., "FABRIC-X-CHECKERED"

    @Column(name = "actual_output_quantity", precision = 10, scale = 2)
    private BigDecimal actualOutputQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status = JobStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    // Helper method to add inputs
    public void addInput(ProductionJobInput input) {
        inputs.add(input);
        input.setProductionJob(this);
    }
}