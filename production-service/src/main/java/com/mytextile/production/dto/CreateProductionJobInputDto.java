package com.mytextile.production.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateProductionJobInputDto(
    @NotEmpty(message = "Input SKU cannot be empty")
    String inputItemSku,

    @NotNull(message = "Input quantity cannot be null")
    @Positive(message = "Input quantity must be positive")
    BigDecimal quantity
) {}