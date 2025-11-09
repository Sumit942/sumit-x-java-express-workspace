package com.mytextile.inventory.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record InwardRequestDto(
    @NotNull(message = "Order ID cannot be null")
    Long orderId,
    @NotNull(message = "Client ID cannot be null")
    Long clientId,
    @NotEmpty(message = "SKU cannot be empty")
    String sku,
    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    BigDecimal quantity
) {}