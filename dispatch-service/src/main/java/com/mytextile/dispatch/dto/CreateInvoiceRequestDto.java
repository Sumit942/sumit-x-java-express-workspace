package com.mytextile.dispatch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

// DTO to create a new invoice
public record CreateInvoiceRequestDto(
    @NotNull(message = "Shipment ID cannot be null")
    Long shipmentId,

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    BigDecimal amount
) {}