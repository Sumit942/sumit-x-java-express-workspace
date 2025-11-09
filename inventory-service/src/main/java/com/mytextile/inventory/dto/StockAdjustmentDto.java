package com.mytextile.inventory.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record StockAdjustmentDto(
    @NotEmpty(message = "SKU cannot be empty")
    String sku,
    @NotNull(message = "Quantity cannot be null (can be positive or negative)")
    BigDecimal quantity,
    @NotEmpty(message = "Reason cannot be empty")
    String reason // Not stored, just for logging
) {}