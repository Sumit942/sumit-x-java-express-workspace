package com.mytextile.production.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "production_job_inputs")
public class ProductionJobInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobInputId;

    // This links back to the parent job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private ProductionJob productionJob;

    @Column(name = "input_item_sku", nullable = false)
    private String inputItemSku; // e.g., "YARN-A-BLUE"

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // e.g., 50.5 (Kg)
}