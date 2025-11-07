package com.mytextile.production.dto;

import com.mytextile.production.entity.JobStatus;
import com.mytextile.production.entity.Machine;

import java.math.BigDecimal;
import java.util.List;

public record ProductionJobDto (
        Long jobId,
        Long orderId, // Logical reference to Order Service
        Machine machine,
        List<ProductionJobInputDto> inputs,
        String outputItemSku, // e.g., "FABRIC-X-CHECKERED"
        BigDecimal actualOutputQuantity,
        JobStatus status
) {}
