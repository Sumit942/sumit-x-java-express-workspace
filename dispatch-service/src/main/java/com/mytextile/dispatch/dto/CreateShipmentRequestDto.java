package com.mytextile.dispatch.dto;
// (As provided before)
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record CreateShipmentRequestDto(
    @NotNull Long orderId,
    @NotNull Long clientId,
    @NotEmpty String shippingAddress,
    @Valid @NotEmpty List<ShipmentItemRequestDto> items
) {}