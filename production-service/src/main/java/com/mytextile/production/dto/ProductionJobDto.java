package com.mytextile.production.dto;

import com.mytextile.production.entity.JobStatus;
import com.mytextile.production.entity.Machine;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ProductionJobDto (
        Long jobId,

        @NotNull(message = "Please provide the order ID")
        Long orderId, // Logical reference to Order Service

        Machine machine,

        List<ProductionJobInputDto> inputs,

        @NotEmpty(message = "Please provide the order item stock keeping unit")

        String outputItemSku, // e.g., "FABRIC-X-CHECKERED"

        @Min(value = 1, message = "Quantity should be greater than zero")
        BigDecimal actualOutputQuantity,

        JobStatus status
) {}
