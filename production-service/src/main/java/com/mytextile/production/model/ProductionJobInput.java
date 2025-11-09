package com.mytextile.production.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "production_job_inputs")
public class ProductionJobInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobInputId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private ProductionJob productionJob;

    @Column(name = "input_item_sku", nullable = false)
    private String inputItemSku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
}