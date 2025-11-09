package com.mytextile.dispatch.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

// DTO for creating a new shipment item (part of the CreateShipmentRequestDto)
public record ShipmentItemRequestDto(
    @NotEmpty(message = "Item SKU cannot be empty")
    String itemSku,

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    BigDecimal quantity
) {}