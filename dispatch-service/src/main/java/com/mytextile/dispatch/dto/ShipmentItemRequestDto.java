package com.mytextile.dispatch.dto;
// (As provided before)
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ShipmentItemRequestDto(
    @NotEmpty String itemSku,
    @NotNull @Positive BigDecimal quantity
) {}