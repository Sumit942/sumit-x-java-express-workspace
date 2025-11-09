package com.mytextile.production.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CompleteJobRequestDto(
    @NotNull(message = "Actual output quantity cannot be null")
    @Positive(message = "Output quantity must be a positive number")
    BigDecimal actualOutputQuantity
) {}